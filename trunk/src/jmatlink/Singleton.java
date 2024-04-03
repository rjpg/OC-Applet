package jmatlink;


/** Singleton.java */
public class Singleton
{
    private static Singleton singleton = null;
    IOMatLab mt=null;

    /** Singleton so constructor is private. */
    private
    Singleton()
    {
		mt=new IOMatLab();
		mt.openMatLab();
    }

    public static Singleton
    getInstance()
    {
        if (singleton == null) {
            singleton = new Singleton();
        }
        return (singleton);
    }

    public IOMatLab
    getValue()
    {
        return mt;
    }

   
}