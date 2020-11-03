package kf.buss.aits20.dmz.gateway.config;

import java.net.URI;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import lombok.Data;

@Data
public class ProxySetting {

	private String path;
	
	private String target;
	
	public boolean isMatchPath(String requestPath) {
		Pattern ptn = Pattern.compile(path);
		return ptn.matcher(requestPath).find();
	}
	
	public String getRedirectPath(URI uri) {
		return StringUtils.hasText(uri.getQuery())
				? target + uri.getPath() + "?" + uri.getQuery()
				: target + uri.getPath();
	}
	
}
