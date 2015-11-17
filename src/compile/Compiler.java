package compile;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 *
 * @author dikshant
 */
public class Compiler {

    private String filePath;
    private String fileName;

    public Compiler() {
    }

    public Compiler(String filePath, String fileName) {
        this.filePath = filePath;
        this.fileName = fileName;
    }

    public void compile() {
        boolean flag = checkCompilation();
        if (flag) {
            performExecution();
        } else {
            showCompilationError();
        }
    }

    public boolean checkCompilation() {
        try {
            Runtime r = Runtime.getRuntime();
            r.exec("cmd /c del " + getFilePath() + "\\" + "a.exe");
            Process compile = Runtime.getRuntime().exec("cmd /c g++ " + getFilePath() + "\\" + getFileName());
            compile.waitFor();
            if (compile.exitValue() == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println("Exception : " + e);
        }
        return false;
    }

    public void showCompilationError() {
        try {
            Runtime r = Runtime.getRuntime();
            BufferedWriter br = new BufferedWriter(new FileWriter("out1.bat"));
            br.write("cd " + getFilePath());
            br.newLine();
            br.write("cmd /c g++ " + getFileName());
            br.flush();
            Process p = r.exec("cmd /c start /wait out1.bat");
            p.waitFor();
            r.exec("cmd /c del out1.bat");
        } catch (Exception e) {
            System.out.println("Exception : " + e);
        }
    }

    public void performExecution() {
        try {
            Runtime r = Runtime.getRuntime();
            BufferedWriter br = new BufferedWriter(new FileWriter("out2.bat"));
            br.write("cmd /c cd " + getFilePath());
            br.newLine();
            br.write("a.exe");
            br.flush();
            Process p = r.exec("cmd /c start /wait out2.bat");
            p.waitFor();
            r.exec("cmd /c del out1.bat");
            r.exec("cmd /c del out2.bat");
            r.exec("cmd /c del a.exe");
        } catch (Exception e) {
            System.out.println("Exception : " + e);
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}
/*
public class Compile {

    public static void main(String[] args) {
        Compiler c = new Compiler();
        String path = "C:\\Users\\dikshant\\Documents\\NetBeansProjects\\Compile\\tmp";
        String fileName = "a.c";
        c.setFilePath(path);
        c.setFileName(fileName);
        c.compile();
    }
}
*/