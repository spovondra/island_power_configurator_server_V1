package com.islandpower.configurator.model;

import com.islandpower.configurator.model.project.Appliance;
import com.islandpower.configurator.model.project.Site;
import com.islandpower.configurator.model.project.SolarComponents;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "projects")
public class Project {

    @Id
    private String id;
    private String name;
    private String userId;
    private List<Appliance> appliances;
    private Site site;
    private SolarComponents solarComponents;
    private double totalAcEnergy;
    private double totalDcEnergy;
    private String systemVoltage;
    private String recommendedSystemVoltage; // Recommended system voltage
    private double totalAcPeakPower; // Total peak power for AC
    private double totalDcPeakPower; // Total peak power for DC

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Appliance> getAppliances() {
        return appliances;
    }

    public void setAppliances(List<Appliance> appliances) {
        this.appliances = appliances;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public SolarComponents getSolarComponents() {
        return solarComponents;
    }

    public void setSolarComponents(SolarComponents solarComponents) {
        this.solarComponents = solarComponents;
    }

    public double getTotalAcEnergy() {
        return totalAcEnergy;
    }

    public void setTotalAcEnergy(double totalAcEnergy) {
        this.totalAcEnergy = totalAcEnergy;
    }

    public double getTotalDcEnergy() {
        return totalDcEnergy;
    }

    public void setTotalDcEnergy(double totalDcEnergy) {
        this.totalDcEnergy = totalDcEnergy;
    }

    public String getSystemVoltage() {
        return systemVoltage;
    }

    public void setSystemVoltage(String systemVoltage) {
        this.systemVoltage = systemVoltage;
    }

    public String getRecommendedSystemVoltage() {
        return recommendedSystemVoltage;
    }

    public void setRecommendedSystemVoltage(String recommendedSystemVoltage) {
        this.recommendedSystemVoltage = recommendedSystemVoltage;
    }

    public double getTotalAcPeakPower() {
        return totalAcPeakPower;
    }

    public void setTotalAcPeakPower(double totalAcPeakPower) {
        this.totalAcPeakPower = totalAcPeakPower;
    }

    public double getTotalDcPeakPower() {
        return totalDcPeakPower;
    }

    public void setTotalDcPeakPower(double totalDcPeakPower) {
        this.totalDcPeakPower = totalDcPeakPower;
    }
}
