package kz.kbtu.edu.ejb;

import java.io.IOException;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class TestClient {
    public final static String JMS_CONNECTION_FACTORY_JNDI="jms/RemoteConnectionFactory";
    public final static String JMS_QUEUE_JNDI="jms/queue/MyQueue";
    public final static String JMS_USERNAME="TestUser";       //  The role for this user is "guest" in ApplicationRealm
    public final static String JMS_PASSWORD="KbtuNumber1!";  
    public final static String WILDFLY_REMOTING_URL="http-remoting://localhost:8080";
   
    private QueueConnectionFactory qconFactory;
    private QueueConnection qcon;
    private QueueSession qsession;
    private QueueSender qsender;
    private Queue queue;
    private TextMessage msg;
   
    public static void main(String[] args) throws Exception {
      InitialContext ic = getInitialContext();
      TestClient queueSender = new TestClient();
      queueSender.init(ic, JMS_QUEUE_JNDI);
      readAndSend(queueSender);
      queueSender.close();
    }
   
    public void init(Context ctx, String queueName) throws NamingException, JMSException {
      qconFactory = (QueueConnectionFactory) ctx.lookup(JMS_CONNECTION_FACTORY_JNDI);
       
      //  If you won't pass jms credential here then you will get 
      // [javax.jms.JMSSecurityException: HQ119031: Unable to validate user: null]    
      qcon = qconFactory.createQueueConnection(this.JMS_USERNAME, this.JMS_PASSWORD);   
       
      qsession = qcon.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
      queue = (Queue) ctx.lookup(queueName);
      qsender = qsession.createSender(queue);
      msg = qsession.createTextMessage();
      qcon.start();
    }
   
    public void send(String message,int counter) throws JMSException {
      msg.setText(message);
      msg.setIntProperty("counter", counter);
      qsender.send(msg);
    }
   
    public void close() throws JMSException {
      qsender.close();
      qsession.close();
      qcon.close();
    }
   
    private static void readAndSend(TestClient wildFlyJmsQueueSender) throws IOException, JMSException {
      String line="Test Message Body with counter = ";
      for(int i=0;i<10;i++) {
            wildFlyJmsQueueSender.send(line+i,i);
            System.out.println("JMS Message Sent: "+line+i+"\n");
         }
    }
   
    private static InitialContext getInitialContext() throws NamingException {
       InitialContext context=null;
       try {
             Properties props = new Properties();
             props.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
             props.put(Context.PROVIDER_URL, WILDFLY_REMOTING_URL);   // NOTICE: "http-remoting" and port "8080"
             props.put(Context.SECURITY_PRINCIPAL, JMS_USERNAME);
             props.put(Context.SECURITY_CREDENTIALS, JMS_PASSWORD);
             //props.put("jboss.naming.client.ejb.context", true);
             context = new InitialContext(props); 
             System.out.println("\n\tGot initial Context: "+context);     
        } catch (Exception e) {
             e.printStackTrace();
        }
      return context;
    }
  }