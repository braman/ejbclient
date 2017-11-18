package kz.kbtu.edu.ejb;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import tengrinews.beans.INewsRemote;
import tengrinews.dto.NewsDTO;

public class HelloBeanTest {

    public static void main(String[] args) throws NamingException {
        
        Properties props = new Properties();
        props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        
        InitialContext ctx = new InitialContext(props);
        Object obj = ctx.lookup("ejb:tengrinews/NewsBean!tengrinews.beans.INewsRemote");
        if (obj instanceof INewsRemote) {
            INewsRemote remote = (INewsRemote) obj;

            System.out.println("Invoking remote bean");
            List<NewsDTO> top10News = remote.getTop10News();
            System.out.println("Invoking finished!");
            
            Optional.ofNullable(top10News).orElse(Collections.emptyList()).stream().forEach(System.out::println);
            
        } else {
            System.err.println("Can't lookup service");
        }
        
        
        
        
    }
    
}
