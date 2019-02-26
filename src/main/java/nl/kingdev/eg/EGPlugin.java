package nl.kingdev.eg;

import nl.kingdev.eg.logger.Logger;
import nl.kingdev.eg.tasks.DecompileTask;
import nl.kingdev.eg.tasks.PatchTask;
import nl.kingdev.eg.tasks.RunGameTask;
import nl.kingdev.eg.tasks.SetupTask;
import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.io.File;

public class EGPlugin implements Plugin<Project> {
    public Project project;
    public static String OS = System.getProperty("os.name").toLowerCase();


    public static File libDir = new File("lib");
    public static File gameJar = new File("lib/Equilinox.jar");


    @Override
    public void apply(Project project) {
        this.project = project;
        Logger.info("Loading up EquilinoxGradle V1 OS:" + OS);

        libDir = new File(project.getProjectDir(), "lib");
        gameJar = new File(project.getProjectDir(), "lib/Equilinox.jar");

        if (!libDir.exists()) {
            libDir.mkdirs();
        }

        if (!gameJar.exists()) {
            throw new GradleException("Please provide an Equilinox.jar in the lib folder!");
        }
        //Be sure that lib/Equilinox exists, as it will hold the unpatched SRC.
        new File(project.getProjectDir(), "lib/Equilinox").mkdirs();

        setupTasks();

    }

    private void setupTasks() {

        project.getTasks().create("setup", SetupTask.class, setupTask -> {
            setupTask.project = project;
            setupTask.setGroup("equilinox");
        });

        project.getTasks().create("decompile", DecompileTask.class, decompileTask -> {
            decompileTask.project = project;
            decompileTask.setGroup("equilinox");
        });
        project.getTasks().create("patch", PatchTask.class, patchTask -> {
            patchTask.project = project;
            patchTask.setGroup("equilinox");
        });

        project.getTasks().create("runGame", RunGameTask.class, runGameTask -> {
            runGameTask.project = project;

            runGameTask.setGroup("equilinox");
            runGameTask.dependsOn("build");
        });
    }

}
