package kz.kbtu.edu.ejb;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import kz.kbtu.edu.ejb.demo.HelloBeanRemote;

public class HelloBeanTest {

    public static void main(String[] args) throws NamingException {
        
        Properties props = new Properties();
        props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        
        InitialContext ctx = new InitialContext(props);
        Object obj = ctx.lookup("ejb:demoear/demoejb/hello!kz.kbtu.edu.ejb.demo.HelloBeanRemote");
        if (obj instanceof HelloBeanRemote) {
            HelloBeanRemote remote = (HelloBeanRemote) obj;
            System.out.println("Invoking remote bean");
            remote.sayHello("Almaty");
            System.out.println("Invoking finished!");
            
        } else {
            System.err.println("Can't lookup service");
        }
        
        
        
        
    }
    
}
