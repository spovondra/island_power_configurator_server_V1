package com.islandpower.configurator.service;

import com.islandpower.configurator.model.OneUser;
import com.islandpower.configurator.model.Project;
import com.islandpower.configurator.model.project.Site;
import com.islandpower.configurator.repository.ProjectRepository;
import com.islandpower.configurator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing projects and their associated operations.
 * This class provides methods to create, delete, update, and retrieve projects,
 * along with validating user permissions.
 *
 * @version 1.0
 */
@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Creates a new project and associates it with a user.
     *
     * @param project The project object to create
     * @param userId  The ID of the user who owns the project
     * @return Project The newly created project object
     */
    public Project createProject(Project project, String userId) {
        project.setUserId(userId);
        Project savedProject = projectRepository.save(project);

        OneUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        user.getProjects().add(savedProject.getId());
        userRepository.save(user);

        return savedProject;
    }

    /**
     * Deletes an existing project, ensuring the user has the necessary permissions.
     *
     * @param projectId The ID of the project to delete
     * @param userId    The ID of the user performing the deletion
     */
    public void deleteProject(String projectId, String userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        OneUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        boolean isAdmin = user.getRole().contains("ADMIN");
        if (!isAdmin && !project.getUserId().equals(userId)) {
            throw new RuntimeException("User does not have permission to delete this project.");
        }

        projectRepository.deleteById(projectId);
        user.getProjects().remove(projectId);
        userRepository.save(user);
    }

    /**
     * Retrieves all projects in the system.
     *
     * @return List<Project> A list of all projects
     */
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    /**
     * Retrieves all projects associated with a specific user.
     *
     * @param userId The ID of the user whose projects to retrieve
     * @return List<Project> A list of projects associated with the user
     */
    public List<Project> getProjectsByUserId(String userId) {
        OneUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        return projectRepository.findAllById(user.getProjects());
    }

    /**
     * Retrieves a project by its ID, ensuring the user has permission to access it.
     *
     * @param projectId The ID of the project to retrieve
     * @param userId    The ID of the user requesting the project
     * @return Project The requested project
     * @throws RuntimeException if the project is not found or the user lacks permission
     */
    public Project getProjectById(String projectId, String userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        OneUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        boolean isAdmin = user.getRole().contains("ADMIN");
        if (!isAdmin && !project.getUserId().equals(userId)) {
            throw new RuntimeException("User does not have permission to access this project.");
        }

        return project;
    }

    /**
     * Updates an existing project, ensuring the user has permission to modify it.
     *
     * @param projectId      The ID of the project to update
     * @param updatedProject The updated project object
     * @param userId         The ID of the user making the update
     * @return Project The updated project
     */
    public Project updateProject(String projectId, Project updatedProject, String userId) {
        Project existingProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        OneUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        boolean isAdmin = user.getRole().contains("ADMIN");
        if (!isAdmin && !existingProject.getUserId().equals(userId)) {
            throw new RuntimeException("User does not have permission to update this project.");
        }

        existingProject.setName(updatedProject.getName());
        existingProject.setSite(updatedProject.getSite());
        existingProject.setConfigurationModel(updatedProject.getConfigurationModel());

        return projectRepository.save(existingProject);
    }

    /**
     * Updates the site information for a project, including location data.
     *
     * @param projectId            The ID of the project to update
     * @param userId               The ID of the user performing the update
     * @param latitude             The latitude of the project location
     * @param longitude            The longitude of the project location
     * @param minMaxTemperatures   The minimum and maximum temperatures for the site
     * @param panelAngle           The angle of the solar panels
     * @param panelAspect          The aspect (orientation) of the solar panels
     * @param usedOptimalValues    Whether optimal values were used for the configuration
     * @param monthlyData          The monthly solar irradiance and temperature data
     */
    public void updateSiteWithLocationData(String projectId, String userId, double latitude, double longitude,
                                           double[] minMaxTemperatures, int panelAngle, int panelAspect,
                                           boolean usedOptimalValues, List<Site.MonthlyData> monthlyData) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        OneUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        boolean isAdmin = user.getRole().contains("ADMIN");
        if (!isAdmin && !project.getUserId().equals(userId)) {
            throw new RuntimeException("User does not have permission to update this project.");
        }

        Site site = project.getSite();
        if (site == null) {
            site = new Site();
            project.setSite(site);
        }

        site.setLatitude(latitude);
        site.setLongitude(longitude);
        site.setMinTemperature(minMaxTemperatures[0]);
        site.setMaxTemperature(minMaxTemperatures[1]);
        site.setPanelAngle(panelAngle);
        site.setPanelAspect(panelAspect);
        site.setUsedOptimalValues(usedOptimalValues);
        site.setMonthlyDataList(monthlyData);

        projectRepository.save(project);
    }

    /**
     * Updates the last completed step in the project.
     *
     * @param projectId The ID of the project
     * @param step      The step number to set as the last completed step
     */
    public void updateLastCompletedStep(String projectId, int step) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        project.setLastCompletedStep(step);
        projectRepository.save(project);
    }
}
