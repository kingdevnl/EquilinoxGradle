package nl.kingdev.eg.tasks;

import org.gradle.api.Project;
import org.gradle.api.tasks.JavaExec;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RunGameTask extends JavaExec {
    public Project project;

    @Override
    public void exec() {
        File workDir = new File("run/");
        workDir.mkdirs();
        workingDir(workDir);

        List<String> libs = new ArrayList<>();

        libs.add(new File(project.getProjectDir(), "build/libs/Equilinox.jar").getAbsolutePath());

        classpath(libs);
        File natives = new File(project.getProjectDir(), "natives");

        jvmArgs("-Djava.library.path=" + natives.getAbsolutePath());


        super.exec();
    }

    @Override
    public String getMain() {
        return "main.MainApp";
    }
}
