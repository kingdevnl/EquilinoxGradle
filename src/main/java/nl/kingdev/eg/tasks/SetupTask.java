package nl.kingdev.eg.tasks;

import nl.kingdev.eg.logger.Logger;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.TaskAction;

import java.io.File;

public class SetupTask extends DefaultTask {

    public Project project;

    @TaskAction
    void setup() {
        Logger.info("Setting up workspace");

        new File(project.getProjectDir(), "src/main/equilinox").mkdirs();
        new File(project.getProjectDir(), "src/main/eml").mkdirs();
        new File(project.getProjectDir(), "src/main/resources").mkdirs();
        new File(project.getProjectDir(), "src/main/resources/res").mkdirs();


        DecompileTask decompileTask = (DecompileTask) project.getTasks().getByName("decompile");

        decompileTask.decompile();
    }

}
