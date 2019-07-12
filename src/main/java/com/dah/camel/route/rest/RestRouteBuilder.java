package com.dah.camel.route.rest;

import com.dah.camel.domain.user.User;
import com.dah.camel.domain.user.UserMapper;
import com.dah.camel.domain.user.UserService;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class RestRouteBuilder extends SpringRouteBuilder {

    /*@Autowired
    private Environment env;

    @Value("${camel.component.servlet.mapping.context-path}")
    private String contextPath;*/

    @Override
    public void configure() throws Exception {

        //rest://method:path[:uriTemplate]?[options]
        //bindingMode(RestBindingMode.json) 绑定模型为json
        //restConfiguration().component("jetty").host("127.0.0.1").port(8080).bindingMode(RestBindingMode.json);

        //restConfiguration().host("127.0.0.1").port(8001);
        //restConfiguration().component("jetty").host("127.0.0.1").port(8081).bindingMode(RestBindingMode.json);

        /*from("rest:get:hello")
                .transform().constant("Bye World");

        from("rest:get:hello/{me}")
                .transform().simple("Bye ${header.me}");*/

        /*from("rest:get:hello:/{me}")
                .transform().simple("Hi ${header.me}");*/

        /*from("rest:get:hello:/french/{me}")
                .transform().simple("Bonjour ${header.me}");*/

//          from("jetty:http://127.0.0.1:8080/start")
//                .to(("rest:get:hello/person?bridgeEndpoint=true"));

         from("timer:hello-user?period=10s")
                .to("rest:get:hello/person1")
                .log("${body}");

       //将http://127.0.0.1:8001/users 接口 发布到 http://127.0.0.1:8081/camel/users
//        from("rest:get:/camel/users")
//                .to("jetty:http://127.0.0.1:8001/users?bridgeEndpoint=true");

        //put 和 post 方式暂未测试成功

        //consumes()  接受媒体类型   application/json 或者 text/xml 默认为全部
        //produces()  返回媒体类型  application/json 或者 text/xml 默认为全部

        /*rest("/users")
                *//*.consumes("application/json")
                .produces("application/json")*//*

                .get().to("bean:userMapper?method=getAll")

                //调用bean之前需要注入相应的bean、调用的方法为 public 修饰的
                .get("/{id}")
                .param().name("id").type(RestParamType.path).endParam()
                .to("bean:userMapper?method=getById(${header.id})")

                .put("/{id}").type(User.class)
                .param().name("id").type(RestParamType.path).endParam()
                .param().name("body").type(RestParamType.body).endParam()
                .to("bean:userMapper?method=update(${header.id}, ${body})")
                // 直接写 Mapper 的方法的话，json参数需要带上id
                //{ "username" : "admin", "password" : "{bcrypt}$2a$10$q8CZYHgxkNu4nwp94ddSQueoi0POscOVdZNE2q8bt.NPqWvzig3yu"}

                .post().type(User.class)
                .param().name("body").type(RestParamType.body).endParam()
                .to("bean:userMapper?method=save(${body})");
                //{"username" : "root", "password" : "{bcrypt}$2a$10$q8CZYHgxkNu4nwp94ddSQueoi0POscOVdZNE2q8bt.NPqWvzig3yu"}*/

    }
}
