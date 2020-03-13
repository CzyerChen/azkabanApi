/**
 * Author:   claire
 * Date:    2020-03-13 - 09:41
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-03-13 - 09:41          V1.3.4
 */
package com.fibodt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 功能简述 <br/> 
 * 〈〉
 *
 * @author claire
 * @date 2020-03-13 - 09:41
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "azkaban")
public class AzkabanConfig {
    private String username;
    private String password;
    private String host;
    private String port;
}
