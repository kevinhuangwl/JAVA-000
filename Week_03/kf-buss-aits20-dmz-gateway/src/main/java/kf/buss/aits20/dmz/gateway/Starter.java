package kf.buss.aits20.dmz.gateway;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import kf.buss.aits20.dmz.gateway.config.ProxySetting;
import kf.buss.aits20.dmz.gateway.filter.HttpRequestFilter;

@Component
public class Starter implements CommandLineRunner {

	@Value("${server.port}")
	private int port;
	
	@Autowired
	private List<HttpRequestFilter> filterList;
	
	@Autowired
	private List<ProxySetting> proxyList;
	
	@Override
	public void run(String... args) throws Exception {
		HttpServer server = new HttpServer(port, filterList, proxyList);
		server.startup();
	}

}
