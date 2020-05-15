package oms.pc_protector.restApi.policy.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ConfigurationVO {

    private boolean vaccineInstallationExecution;
    private boolean vaccinePatch;
    private boolean osWithMsOfficePatch;
    private boolean hwpPatch;
    private boolean passwordStability;
    private boolean passwordChange;
    private boolean screenSaver;
    private boolean sharedFolder;
    private boolean usbAutoRun;
    private boolean overDateActiveX;
    private boolean editProgram;
    private boolean wirelessLanCard;
    private boolean securityUsb;
    private boolean acrobatSecurityPatch;
    private boolean unApprovedProcess;
    private boolean requiredProcess;
}
