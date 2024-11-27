package com.islandpower.configurator.service.project;

import com.islandpower.configurator.model.project.Appliance;
import com.islandpower.configurator.model.Project;
import com.islandpower.configurator.model.project.ConfigurationModel;
import com.islandpower.configurator.model.project.ProjectAppliance;
import com.islandpower.configurator.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProjectApplianceService {

    @Autowired
    private ProjectRepository projectRepository;

    // Přidání nebo aktualizace spotřebiče v projektu
    public Project addOrUpdateAppliance(String projectId, Appliance appliance) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Projekt nebyl nalezen: " + projectId));

        List<Appliance> appliances = project.getAppliances();
        if (appliances == null) {
            appliances = new ArrayList<>();
            project.setAppliances(appliances);
        }

        // Zajištění ID spotřebiče
        if (appliance.getId() == null || appliance.getId().isEmpty()) {
            appliance.setId(UUID.randomUUID().toString());
        }

        Optional<Appliance> existingAppliance = appliances.stream()
                .filter(a -> a.getId().equals(appliance.getId()))
                .findFirst();

        if (existingAppliance.isPresent()) {
            appliances.remove(existingAppliance.get());
        }
        appliances.add(appliance);

        // Přepočet energie
        calculateEnergy(project);

        return projectRepository.save(project);
    }

    // Odstranění spotřebiče z projektu
    public void removeAppliance(String projectId, String applianceId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Projekt nebyl nalezen: " + projectId));

        List<Appliance> appliances = project.getAppliances();
        if (appliances != null) {
            appliances.removeIf(appliance -> appliance.getId().equals(applianceId));
        }

        calculateEnergy(project);

        projectRepository.save(project);
    }

    // Výpočet celkové energie projektu
    private void calculateEnergy(Project project) {
        ConfigurationModel configModel = project.getConfigurationModel();
        if (configModel == null) {
            configModel = new ConfigurationModel();
            project.setConfigurationModel(configModel);
        }

        ProjectAppliance projectAppliance = configModel.getProjectAppliance();
        if (projectAppliance == null) {
            projectAppliance = new ProjectAppliance();
            configModel.setProjectAppliance(projectAppliance);
        }

        double totalAcPower = 0;
        double totalDcPower = 0;
        double totalAcEnergyDaily = 0; // Celková denní energie AC
        double totalDcEnergyDaily = 0; // Celková denní energie DC
        double totalAcPeakPower = 0;
        double totalDcPeakPower = 0;

        List<Appliance> appliances = project.getAppliances();
        if (appliances != null) {
            for (Appliance appliance : appliances) {
                // Výpočet týdenní energie: E_week = P_appliance ⋅ t_hours ⋅ t_days
                double energyWeekly = appliance.getPower() * appliance.getHours() * appliance.getDays();

                // Výpočet denní energie: E_day = E_week / 7
                double energyDaily = energyWeekly / 7;

                // Uložení denní energie do spotřebiče
                appliance.setEnergy(energyDaily * appliance.getQuantity());

                // Třídění podle typu spotřebiče
                if (appliance.getType() != null && appliance.getType().equals("AC")) {
                    totalAcPower += appliance.getPower() * appliance.getQuantity();
                    totalAcEnergyDaily += energyDaily * appliance.getQuantity();
                    totalAcPeakPower += appliance.getPeakPower() * appliance.getQuantity();
                } else if (appliance.getType() != null && appliance.getType().equals("DC")) {
                    totalDcPower += appliance.getPower() * appliance.getQuantity();
                    totalDcEnergyDaily += energyDaily * appliance.getQuantity();
                    totalDcPeakPower += appliance.getPeakPower() * appliance.getQuantity();
                }
            }
        }

        projectAppliance.setTotalAcPower(totalAcPower);
        projectAppliance.setTotalDcPower(totalDcPower);
        projectAppliance.setTotalAcEnergy(totalAcEnergyDaily);
        projectAppliance.setTotalDcEnergy(totalDcEnergyDaily);
        projectAppliance.setTotalAcPeakPower(totalAcPeakPower);
        projectAppliance.setTotalDcPeakPower(totalDcPeakPower);

        // Doporučené systémové napětí na základě celkové denní energie: E_daily_total = ∑E_day_i
        configModel.setRecommendedSystemVoltage(calculateRecommendedSystemVoltage(totalAcEnergyDaily + totalDcEnergyDaily));
    }

    // Výpočet doporučeného systémového napětí
    private double calculateRecommendedSystemVoltage(double totalEnergyDaily) {
        // Doporučení napětí na základě celkové denní energie
        if (totalEnergyDaily < 1000) {
            return 12;
        } else if (totalEnergyDaily < 3000) {
            return 24;
        } else {
            return 48;
        }
    }
}
