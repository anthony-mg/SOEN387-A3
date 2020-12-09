package test;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import businesslayer.factory.UserManagerFactory;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import servlets.LoginServlet;
import usermanagerIMP.UserManager;

import java.io.IOException;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertTrue;

public class LoginServletTest {

    private LoginServlet servlet;

    private HttpServletRequest mockRequest;

    private HttpServletResponse mockResponse;


    @Before
    public void setUp() {
        servlet = new LoginServlet();
        //UserManager um = UserManagerFactory.getInstance().create(mockRequest.getServletContext().getRealPath("/WEB-INF/users.json").toString(), mockRequest.getServletContext().getRealPath("/WEB-INF/groups_definition.json").toString());
        mockRequest = createMock(HttpServletRequest.class);
        mockResponse = createMock(HttpServletResponse.class);
    }

    @After
    public void tearDown() {
        verify(mockRequest);
        verify(mockResponse);
    }

    @Test
    public void testDoPostHttpServletRequestHttpServletResponse() {
        mockRequest.getParameter("email");
        expectLastCall().andReturn("mammal1@gmail.com");
        mockRequest.getParameter("password");
        expectLastCall().andReturn("mammal1");

        replay(mockRequest);
        replay(mockResponse);
        try {
            servlet.doPost(mockRequest, mockResponse);
        } catch (ServletException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertTrue(true);

    }
}

