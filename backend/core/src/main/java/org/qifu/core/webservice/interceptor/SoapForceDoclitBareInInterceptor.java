package org.qifu.core.webservice.interceptor;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.service.model.ServiceInfo;

public class SoapForceDoclitBareInInterceptor extends AbstractPhaseInterceptor<Message> {
	
	public SoapForceDoclitBareInInterceptor() {
		super(Phase.POST_PROTOCOL);
	}

	@Override
	public void handleMessage(Message message) throws Fault {
		Exchange exchange = message.getExchange();
		for (ServiceInfo si : exchange.getService().getServiceInfos()) {
			si.setProperty("soap.force.doclit.bare", true);
		}
	}
	
}
