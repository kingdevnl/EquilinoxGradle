package nl.kingdev.eg.tasks;

import com.cloudbees.diff.ContextualPatch;
import com.cloudbees.diff.PatchException;
import nl.kingdev.eg.EGPlugin;
import nl.kingdev.eg.logger.Logger;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;


import java.io.*;
import java.nio.file.Files;

public class PatchTask extends DefaultTask {

    public Project project;


    @TaskAction
    public void patch() {
        try {

            InputStream resourceAsStream = getClass().getResourceAsStream("/equilinox.patch");
            File patchFile = new File(EGPlugin.libDir.getAbsolutePath()+"/Equilinox/game.patch");
            if(patchFile.exists()) {
                patchFile.delete();
            }
            Files.copy(resourceAsStream, patchFile.toPath());

            ContextualPatch patch = ContextualPatch.create(patchFile, new File(project.getProjectDir(), "lib/Equilinox"));
            patch.patch(false).forEach(patchReport -> {

                if(patchReport.getStatus() == ContextualPatch.PatchStatus.Patched) {
                    System.out.println("Patched: "+patchReport.getFile().getName());
                }
            });


        } catch (IOException e) {
            e.printStackTrace();

            throw new GradleException("Failed to copy patch file!");
        } catch (PatchException e) {
            e.printStackTrace();
        }


        //
//        String patchCommand = "";
//
//        if(EGPlugin.OS.contains("win")) {
//            patchCommand = "cmd /c \"cd lib/Equilinox && git apply  --directory=lib/Equilinox game.patch \"";
//        } else {
//            patchCommand = "bash -c \"pwd && cd lib/Equilinox && git apply  --directory=lib/Equilinox game.patch \"";
//        }
//        System.out.println("PatchCommand: " + patchCommand);
//
//        try {
//            Runtime runtime = Runtime.getRuntime();
//            Process process = runtime.exec(patchCommand);
//            final InputStream in = process.getInputStream();
//            int ch;
//            while ((ch = in.read()) != -1) {
//                System.out.print((char) ch);
//            }
//            in.close();
//            Logger.info("Patching is done.");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }



    }
}
