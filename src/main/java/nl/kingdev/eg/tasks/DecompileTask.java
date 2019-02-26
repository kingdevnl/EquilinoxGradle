package nl.kingdev.eg.tasks;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import nl.kingdev.eg.EGPlugin;
import nl.kingdev.eg.logger.Logger;
import nl.kingdev.eg.utils.DirUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;
import org.jetbrains.java.decompiler.main.decompiler.ConsoleDecompiler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class DecompileTask extends DefaultTask {

    public Project project;

    @TaskAction
    public void decompile() {
        Logger.info("Starting decompile task");
        if (EGPlugin.gameJar.exists()) {
            ConsoleDecompiler.main(new String[]{"-dgs=true", EGPlugin.gameJar.getAbsolutePath(), new File(EGPlugin.libDir, "Equilinox").getAbsolutePath()});

            Logger.info("Decompiling done, Unzipping sources..");
            try {
                ZipFile zipFile = new ZipFile(new File(EGPlugin.libDir, "Equilinox/Equilinox.jar"));
                zipFile.extractAll(new File(EGPlugin.libDir, "Equilinox").getAbsolutePath());
                //Delete files that aren't important for patching and Libraries
                DirUtils.deleteFolder(new File(EGPlugin.libDir, "Equilinox/org"));
                DirUtils.deleteFolder(new File(EGPlugin.libDir, "Equilinox/com"));

                File resDir = new File(EGPlugin.libDir, "Equilinox/res");
                Files.move(resDir.toPath(), new File(project.getProjectDir(), "src/main/resources/res").toPath(), StandardCopyOption.REPLACE_EXISTING);

                new File(EGPlugin.libDir, "Equilinox/Equilinox.jar").delete();
                Logger.info("Decompiling done, Moving to Patch task.");
                PatchTask patchTask = (PatchTask) project.getTasks().getByName("patch");
                patchTask.patch();
            } catch (ZipException e) {
                e.printStackTrace();
                throw new GradleException("Failed to unzip sources!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
