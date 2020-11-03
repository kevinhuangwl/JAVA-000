package kf.buss.aits20.dmz.gateway.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "server-list")
public class ProxySettingConfig {

	List<ProxySetting> server;

	public List<ProxySetting> getServer() {
		return server;
	}

	public void setServer(List<ProxySetting> server) {
		this.server = server;
	}
	
	@Bean
	public List<ProxySetting> createServerList(){
		return server;
	}
}
