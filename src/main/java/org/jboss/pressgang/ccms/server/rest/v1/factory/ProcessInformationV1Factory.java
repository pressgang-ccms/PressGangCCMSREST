package org.jboss.pressgang.ccms.server.rest.v1.factory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import org.jboss.pressgang.ccms.model.Process;
import org.jboss.pressgang.ccms.model.ProcessStatus;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.elements.RESTProcessInformationV1;
import org.jboss.pressgang.ccms.rest.v1.elements.enums.RESTProcessStatusV1;
import org.jboss.pressgang.ccms.rest.v1.elements.enums.RESTProcessTypeV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.async.ProcessManager;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTElementFactory;
import org.jboss.resteasy.spi.BadRequestException;

@ApplicationScoped
public class ProcessInformationV1Factory extends RESTElementFactory<RESTProcessInformationV1, Process> {

    @Inject
    private ProcessManager processManager;

    public RESTProcessInformationV1 createRESTEntityFromObject(final Process process, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand) {
        final RESTProcessInformationV1 retValue = new RESTProcessInformationV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTProcessInformationV1.LOGS_NAME);
        retValue.setExpand(expandOptions);

        retValue.setId(process.getUuid());
        retValue.setName(process.getName());
        retValue.setStartedBy(process.getStartedBy());
        retValue.setStartTime(process.getStartTime());
        retValue.setEndTime(process.getEndTime());
        final ProcessStatus status = processManager.getProcessStatus(process.getUuid(), false);
        retValue.setStatus(RESTProcessStatusV1.valueOf(status == null ? process.getStatus().name() : status.name()));
        retValue.setType(RESTProcessTypeV1.valueOf(process.getType().name()));

        if (expand != null && expand.contains(RESTProcessInformationV1.LOGS_NAME)) {
            retValue.setLogs(process.getLogs());
        }

        retValue.setUrl(baseUrl + (baseUrl.endsWith("/") ? "" : "/") + "1/" + RESTv1Constants.PROCESS_URL_NAME + "/get/" + dataType + "/"
                + process.getUuid());

        return retValue;
    }

    @Override
    public void updateObjectFromRESTEntity(Process object, RESTProcessInformationV1 dataObject) {
        throw new BadRequestException("Processes cannot be updated via the REST API");
    }
}
