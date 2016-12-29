package com.udl.tfg.sposapp;


import com.jcraft.jsch.JSchException;
import com.udl.tfg.sposapp.models.*;
import com.udl.tfg.sposapp.repositories.MethodInfoRepository;
import com.udl.tfg.sposapp.repositories.ModelInfoRepository;
import com.udl.tfg.sposapp.repositories.SessionRepository;
import com.udl.tfg.sposapp.repositories.VirtualMachineRepository;
import com.udl.tfg.sposapp.utils.OCAManager;
import com.udl.tfg.sposapp.utils.SSHManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;

@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private VirtualMachineRepository virtualMachineRepository;
    @Autowired
    private ModelInfoRepository modelInfoRepository;
    @Autowired
    private MethodInfoRepository methodInfoRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private SSHManager sshManager;
    @Autowired
    private OCAManager ocaManager;
    @Value("${localStorageFolder}") private String localStorageFolder;

    Map<MethodCodes, MethodInfo> methods = new HashMap<>();

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        PopulateDB();

        try {
            sshManager.Initialize();
        } catch (JSchException e) {
            e.printStackTrace();
        }

        ocaManager.Initialize();

        new Thread() {

            boolean sessionsHasResults(long id) {
                try {
                    File f = new File(localStorageFolder + "/" + String.valueOf(id) + "/results.txt");
                    byte[] encoded = new byte[0];
                    encoded = Files.readAllBytes(f.toPath());
                    String content = new String(encoded, Charset.defaultCharset());
                    if (!content.isEmpty()) {
                        return true;
                    }
                    return false;
                } catch (Exception e) {
                    return false;
                }
            }

            public void run() {
                while(true) {
                    for (Session session : sessionRepository.findAll()) {
                        if (!session.isVmDestroyed() && sessionsHasResults(session.getId()) && session.getVmConfig().getId() > 3) {
                            try {
                                ocaManager.deleteVM(session.getVmConfig().getApiID());
                                session.setVmDestroyed(true);
                                sessionRepository.save(session);
                            } catch (Exception ignored) {}
                        }
                    }
                    try {
                        Thread.sleep(5*60*1000); //Run every 5 minutes
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }.start();
    }

    private void PopulateDB() {

        System.out.println("Start populateDB");

        CreateVmConfig(1, 1024, 0.5f);
        CreateVmConfig(4, 1024, 1f);
        CreateVmConfig(8, 4096, 2f);

        CreateMethods(MethodCodes.lpsolve, "lpsolve", true, true, false, false,false, false);
        CreateMethods(MethodCodes.glpk, "glpk", true, true, true, false, false, false);
        CreateMethods(MethodCodes.cbc, "cbc",true, true, true, false,false,false);
        CreateMethods(MethodCodes.dip, "dip",false, false, false,true,false, false);
        CreateMethods(MethodCodes.symphony, "symphony", true, true, false, false,false, false);
        CreateMethods(MethodCodes.clp, "clp", true, true, false, false, false,false);


        CreateModels(ModelCodes.Determinist, "Determinist model", new ArrayList<>(
                Arrays.asList(
                        methods.get(MethodCodes.glpk),
                        methods.get(MethodCodes.cbc),
                        methods.get(MethodCodes.lpsolve),
                        methods.get(MethodCodes.clp),
                        methods.get(MethodCodes.dip),
                        methods.get(MethodCodes.symphony)
                )
        ));

        System.out.println("End populateDB");
    }

    private void CreateVmConfig(int virtualCPUs, int ram, float realCPUs) {
        VirtualMachine vm = new VirtualMachine();
        vm.setVirtualCPUs(virtualCPUs);
        vm.setRam(ram);
        vm.setrealCPUs(realCPUs);
        virtualMachineRepository.save(vm);
    }

    private void CreateModels(ModelCodes type, String name, List<MethodInfo> compatibleMethods) {
        ModelInfo mi = new ModelInfo();
        mi.setModel(type);
        mi.setName(name);
        mi.setCompatibleMethods(compatibleMethods);
        modelInfoRepository.save(mi);
    }

    private void CreateMethods(MethodCodes method, String name, boolean mpsSupport, boolean lpSupport,
                               boolean pyomoSupport, boolean decompostionSupport, boolean parallelizationSupport, boolean clusterSupport) {
        MethodInfo mi = new MethodInfo();
        mi.setMethod(method);
        mi.setName(name);
        mi.setMpsSupport(mpsSupport);
        mi.setLpSupport(lpSupport);
        mi.setPyomotSupport(pyomoSupport);
        mi.setDecompositionSupport(decompostionSupport);
        mi.setParallelizationSupport(parallelizationSupport);
        mi.setClusterSupport(clusterSupport);
        methodInfoRepository.save(mi);
        methods.put(method, mi);
    }
}
