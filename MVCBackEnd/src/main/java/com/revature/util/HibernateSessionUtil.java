package com.revature.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateSessionUtil {
	private static SessionFactory sF 
	= new Configuration().configure().buildSessionFactory();
	public static Session getSession() {
		return sF.openSession();
	}
}