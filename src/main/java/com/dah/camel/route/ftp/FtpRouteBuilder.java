package com.dah.camel.route.ftp;

import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class FtpRouteBuilder extends SpringRouteBuilder {
    @Override
    public void configure() throws Exception {
        //from("ftp://127.0.0.1:21?username=ftpuser&password=ciotea@ftp&binary=true&passiveMode=true&delay=2000").routeId("ftpRouteBuilder").autoStartup(false).to("file:D:\\log");

        // 此种方式会失真
        //from("ftp://192.167.0.100:21/example2/info/full_editor?username=ftpuser&password=ciotea@ftp&binary=true&passiveMode=true&delay=2000").to("ftp://127.0.0.1:21?username=ftpuser&password=ciotea@ftp");
        //from("file:D:\\log?delay=2000").process(new FtpProcessor()).to("ftp://127.0.0.1:21?username=ftpuser&password=ciotea@ftp");
        // 设置过滤器，判定是否下载，注意过滤器名称前的‘#’
        from("file:D:\\ftp?delay=2000&noop=true").to("ftp://192.167.6.133:21?username=ftpuser&password=ciotea@ftp&fileExist=override");

        //from("ftp://127.0.0.1:21?username=ftpuser&password=ciotea@ftp&filter=#ftpFilter&binary=true&passiveMode=true&delay=2000").to("file:D:\\log");
    }
}
