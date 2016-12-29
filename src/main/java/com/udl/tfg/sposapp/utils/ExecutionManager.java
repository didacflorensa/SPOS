package com.udl.tfg.sposapp.utils;

import com.udl.tfg.sposapp.models.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ExecutionManager {

    @Autowired
    private SSHManager sshManager;

    @Value("${frontend}") private String frontendIP;
    @Value("${scriptsFolder}") private String scriptsFolder;


    private com.jcraft.jsch.Session sshSession;

    public void LaunchExecution(Session session) throws Exception {
        try {
            sshSession = sshManager.OpenSession(session.getIP(), 22, "root");
            run(session,session.getInfo().getMethod().getMethod().name());
            sshSession.disconnect();
            sshSession = null;
        } catch (Exception e) {
            if (sshSession != null) sshSession.disconnect();
            throw new Exception(e);
        }
    }


    private void run (Session session, String solver) throws  Exception{
        String script;
        if (session.getInfo().getFiles().size() > 1){
            if (session.getInfo().getFiles().get(0).getExtension().equals(".dat")) {
                script = "ts " + scriptsFolder + "/"  +solver+ "/" + solver + "-pyomo %1$s %2$s %3$s %4$s %5$s %6$s %7$s";
            } else {
                script = "ts " + scriptsFolder + "/"  +solver+  "/" + solver + "-deco %1$s %2$s %3$s %4$s %5$s %6$s %7$s";
            }
        } else {
            if (session.getInfo().getFiles().get(0).getExtension().equals("mps")) {
                script = "ts " + scriptsFolder + "/"  +solver+ "/" + solver + "-mps %1$s %2$s %3$s %4$s %5$s %6$s";
            } else {
                script = "ts " + scriptsFolder + "/"  +solver+  "/" + solver + "-lp %1$s %2$s %3$s %4$s %5$s %6$s";
            }
        }
        sshManager.ExecuteCommand(sshSession, String.format(script, session.getId(), session.getKey(), session.getEmail(), session.getInfo().getFiles().get(0).getName(), session.getMaximumDuration(), frontendIP));
    }

}
