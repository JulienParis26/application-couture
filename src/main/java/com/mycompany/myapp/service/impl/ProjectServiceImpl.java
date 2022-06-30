package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Project;
import com.mycompany.myapp.repository.ProjectRepository;
import com.mycompany.myapp.service.ProjectService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Project}.
 */
@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final Logger log = LoggerFactory.getLogger(ProjectServiceImpl.class);

    private final ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Project save(Project project) {
        log.debug("Request to save Project : {}", project);
        return projectRepository.save(project);
    }

    @Override
    public Project update(Project project) {
        log.debug("Request to save Project : {}", project);
        return projectRepository.save(project);
    }

    @Override
    public Optional<Project> partialUpdate(Project project) {
        log.debug("Request to partially update Project : {}", project);

        return projectRepository
            .findById(project.getId())
            .map(existingProject -> {
                if (project.getName() != null) {
                    existingProject.setName(project.getName());
                }
                if (project.getRef() != null) {
                    existingProject.setRef(project.getRef());
                }
                if (project.getCreationDate() != null) {
                    existingProject.setCreationDate(project.getCreationDate());
                }
                if (project.getImage1() != null) {
                    existingProject.setImage1(project.getImage1());
                }
                if (project.getImage1ContentType() != null) {
                    existingProject.setImage1ContentType(project.getImage1ContentType());
                }
                if (project.getImage2() != null) {
                    existingProject.setImage2(project.getImage2());
                }
                if (project.getImage2ContentType() != null) {
                    existingProject.setImage2ContentType(project.getImage2ContentType());
                }
                if (project.getImage3() != null) {
                    existingProject.setImage3(project.getImage3());
                }
                if (project.getImage3ContentType() != null) {
                    existingProject.setImage3ContentType(project.getImage3ContentType());
                }
                if (project.getImage4() != null) {
                    existingProject.setImage4(project.getImage4());
                }
                if (project.getImage4ContentType() != null) {
                    existingProject.setImage4ContentType(project.getImage4ContentType());
                }

                return existingProject;
            })
            .map(projectRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Project> findAll(Pageable pageable) {
        log.debug("Request to get all Projects");
        return projectRepository.findAll(pageable);
    }

    public Page<Project> findAllWithEagerRelationships(Pageable pageable) {
        return projectRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Project> findOne(Long id) {
        log.debug("Request to get Project : {}", id);
        return projectRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Project : {}", id);
        projectRepository.deleteById(id);
    }
}
