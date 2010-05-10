package com.allarphoto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;

import strategiclibrary.service.classfinder.ClassFinderService;
import strategiclibrary.service.classfinder.DefaultClassFinderService;

import com.allarphoto.testObjects.AbstractIntegratedTest;


public class TestSerialization extends AbstractIntegratedTest
{

   public TestSerialization(String arg0)
   {
      super(arg0);
   }
   
   public void testSerializableObjects() throws Exception
   {
      ClassFinderService cf = new DefaultClassFinderService();
      for(Class<Serializable> serClazz : (Collection<Class<Serializable>>)cf.findClassesThatExtend(new Class[]{Serializable.class}))
      {
         Serializable s = serClazz.newInstance();
         ByteArrayOutputStream bytes = new ByteArrayOutputStream();
         ObjectOutputStream oos = new ObjectOutputStream(bytes);
         oos.writeObject(s);
         oos.close();
         ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()));
         Serializable s1 = (Serializable)ois.readObject();
         assertEquals(s.getClass(),s1.getClass());
      }
   }

}
