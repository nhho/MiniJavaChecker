package visitor;
import syntaxtree.*;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Vector;

class Global {
	public static int idRefCnt = 0;
}

// The global Symbol Table that maps class name to Class
class SymbolTable {
  private Hashtable<String, Class> hashtable;
  public int dummyCnt;
  public int failLine;
  public int failColumn;
   
  public SymbolTable() {
    hashtable = new Hashtable<String, Class>();
	dummyCnt = 0;
  }

  // Register the class name and map it to a new class (with its supperclass)
  // Return false if there is a name conflicts. Otherwise return true.
  public boolean addClass(String id, String parent, int line, int column) {
    if(containsClass(id)) {
	  Class tmp = getClass(id);
	  failLine = tmp.line;
	  failColumn = tmp.column;
	  dummyCnt += 1;
	  id = Integer.toString(dummyCnt);
	  hashtable.put(id, new Class(id, parent, line, column, this));
      return false;	    
    } else 
      hashtable.put(id, new Class(id, parent, line, column, this));
    return true;	    
  }

  // Return the Class that previously mapped to the specified name.
  // Return null if the specified is not found.
  public Class getClass(String id) {
    if(containsClass(id)) 
      return (Class)hashtable.get(id);	    
    else 
      return null;
  }

  public boolean containsClass(String id) {
    return hashtable.containsKey(id);
  }

  // Given a variable "id" that is used in method "m" inside class "c", 
  // return the type of the variable. It returns null if the variable
  // is not yet defined.
  // If "m" is null, check only fields in class "c" or in its ancestors.
  // If "c" is null, check only the variables declared in "m".
  public Variable getVar(Method m, Class c, String id) {
 
    if (m != null) {
      // Check if the variable is one of the local variables
      if (m.getVar(id) != null) {
        return m.getVar(id);
      }

	  /*
      // Check if the variable is one of the formal parameters
      if (m.getParam(id) != null){
        return m.getParam(id).type();
      }
	  */
    }
     
    // Try to resolve the name against fields in class
    while(c != null) {
      // Check if the variables is one of the fields in the current class
      if (c.getVar(id) != null) {
        return c.getVar(id);  // Found!
      }
      else // Try its superclass (and their superclasses)
      if (c.parent() == null) {
         c = null;
      }
      else {
        c = getClass(c.parent());
      }
    }
    return null;
  }

  // Return the declared method defined in the class named "cName" 
  // (or in one of its ancestors)
  /*
  public Method getMethod(String id, String cName) {
    Class c = getClass(cName);

    if (c == null) {
      System.out.println("Class " + cName + " not defined");  
      System.exit(0); // Panic!
    }

    // Try to find the declared method along the class hierarchy
    while (c != null) {
      if (c.getMethod(id) != null) {
        return c.getMethod(id);	 // Found!
      }
      else 
      if(c.parent() == null) {
        c = null;
      }
      else {
        c = getClass(c.parent());
      }
    }
	
    System.out.println("Method " + id + " not defined in class " + cName);
	
    System.exit(0);
    return null;
  }
  */

  /*
  // Get the return type of a method declared in a class named "classCope"
  public Type getMethodType(String id, String cName) {
    Method m = getMethod(id, cName);
    if (m != null)
      return m.type();

    return null;
  }
  */

  // Utility method to check if t1 is compatible with t2
  // or if t2 is a subclass of t1
  // Note: This method can be placed in another class
  public boolean compareTypes(Type t2, Type t1) {
    if (t1 == null || t2 == null) return false;
	
    if (t1 instanceof DoubleType && t2 instanceof  DoubleType)
      return true;
    if (t1 instanceof IntegerType && t2 instanceof  IntegerType)
      return true;
    if (t1 instanceof BooleanType && t2 instanceof  BooleanType)
      return true;
    if (t1 instanceof IntArrayType && t2 instanceof IntArrayType)
      return true;

    // If both are classes
	if (!(t1 instanceof IdentifierType) && !(t1 instanceof InstanceType)) return false;
	if (!(t2 instanceof IdentifierType) && !(t2 instanceof InstanceType)) return false;
      Class c = getClass(t2.toString());
      while(c != null) {
        // If two classes has the same name
        if (t1.toString().equals(c.getId())) 
          return true;
        else { // Check the next class along the class heirachy

          if (c.parent() == null)
            return false;

          c = getClass(c.parent());
        }
      }
    return false;	
  }
  
  public boolean compareTypes2(Type t2, Type t1) {
    if (t1 == null || t2 == null) return false;
	if (t2 instanceof IntegerType && t1 instanceof  DoubleType)
      return true;
	return compareTypes(t2, t1);
  }

} // SymbolTable

// Store all properties that describe a class
class Class {

  String id;      // Class name
  Vector<Method> methods;
  Hashtable<String, Variable> fields;
  String parent;  // Superclass's name  (null if there is no superclass)
  Type type;      // An instance of Type that represents this class
  int idRef;
  int line;
  int column;
  int failLine;
  int failColumn;
  public int dummyCnt;
  boolean vis;
  SymbolTable st;
  
  // Model a class named "id" that extend a class name "p"
  // "p" is null if class "id" does has extend any class
  public Class(String id, String p, int aline, int acolumn, SymbolTable stt) {
    this.id = id;
    parent = p;
	line = aline; column = acolumn;
    type = new IdentifierType(id, line, column);
    methods = new Vector<Method>();
    fields = new Hashtable<String, Variable>();
	Global.idRefCnt += 1;
	idRef = Global.idRefCnt;
    dummyCnt = 0;
	vis = false;
	st = stt;
  }
    
  // public Class() {}
  
  public Method getMethodById(int id) {
	  for (int i = 0; i < methods.size(); i++)
		if (((Method)methods.elementAt(i)).idRef == id)
			return (Method)methods.elementAt(i);
		return null;
  }
    
  public String getId(){ return id; }
    
  public Type type(){ return type; }

  // Add a method defined in the current class by registering
  // its name along with its return type.
  // The other properties (parameters, local variables) of the method
  // will be added later
  // 
  // Return false if there is a name conflict (among all method names only)
  public boolean addMethod(String id, Type type, Method m) {
	  Method tmp = getMethod(m);
	  if (tmp != null) {
		  if (st.compareTypes(type, tmp.type))
			tmp = null;
	  }
    if (tmp != null) {
	  failLine = tmp.line;
	  failColumn = tmp.column;
	  dummyCnt += 1;
	  id = Integer.toString(dummyCnt);
	  m.id = id;
      methods.addElement(m);
      return false;   
    }
      methods.addElement(m);
      return true;
  }

  /*
  // Enumeration of method names
  public Enumeration getMethods(){
    return methods.keys();
  }
  */

  // Return the method representation for the specified method 
  public Method getMethod(Method m) {
	  Class tmp = this;
	  while (tmp != null) {
		  for (int i = 0; i < tmp.methods.size(); i++) {
			  Method mm = (Method)tmp.methods.elementAt(i);
			  if (m.id.equals(mm.id) && m.params.size() == mm.params.size()) {
				  for (int j = 0; j <= m.params.size(); j++) {
					  if (j == m.params.size())
						  return mm;
					  else if (!m.getParamAt(j).type.toString().equals(mm.getParamAt(j).type.toString())) 
						  break;
				  }
			  }
		  }
		  if (tmp.parent == null)
				return null;
			else
				tmp = st.getClass(tmp.parent);
	  }
      return null;
  }
  
  public Method getMethod2(String qid, Vector<Type> vt, int aline, int acolumn) {
    String rep = qid + "(";
	for (int i = 0; i < vt.size(); i++) {
		if (i > 0)
			rep += ", ";
		rep += ((Type)vt.elementAt(i)).toString();
	}
	rep += ")";
	Class tmp = this;
	while (true) {
		for (int i = 0; i < tmp.methods.size(); i++) {
			Method m = (Method)tmp.methods.elementAt(i);
		// System.out.println("////TEST checking " + rep + " vs " + m.toString());
			if (m.match(qid, vt)) {
		 // System.out.println("////TEST line " + aline + ",column " + acolumn + ": resolved method " + rep);
	     // System.out.println("////TEST  EXACT  Candidate: line " + m.line + ",column " + m.column + " " + m.toString());
				return m;
			}
		}
		if (tmp.parent == null)
			break;
		tmp = st.getClass(tmp.parent);
	}
	tmp = this;
	Method mm = null;
	Vector<Integer> vv = null;
	while (true) {
		for (int i = 0; i < tmp.methods.size(); i++) {
			Method m = (Method)tmp.methods.elementAt(i);
			Vector<Integer> vi = m.compatibility(qid, vt, st);
			if (vi == null)
				continue;
			if (mm == null) {
				mm = m;
				vv = vi;
			} else {
				int res = compareVector(vv, vi);
				if (res == 0) // equal: overload -> ignore parent class
					continue;
				else if (res == -1) // vv < vi
					continue;
				else if (res == 1) { // vv > vi
					mm = m;
					vv = vi;
				} else { // conflict (res == 2)
					System.out.println("line " + aline + ",column " + acolumn + ": more than one candidate for resolving method " + rep);
					System.out.println("    Candidate 1: line " + mm.line + ",column " + mm.column + " " + mm.toString());
					System.out.println("    Candidate 2: line " + m.line + ",column " + m.column + " " + m.toString());
					return null;
				}
			}
		}
		if (tmp.parent == null)
			break;
		tmp = st.getClass(tmp.parent);
	}
	if (mm == null)
		System.out.println("line " + aline + ",column " + acolumn + ": cannot resolve method " + rep);
	else {
		// System.out.println("////TEST line " + aline + ",column " + acolumn + ": resolved method " + rep);
	    // System.out.println("////TEST    Candidate 1: line " + mm.line + ",column " + mm.column + " " + mm.toString());
	}
	return mm;
  }
  
  public int compareVector(Vector<Integer> a, Vector<Integer> b) {
	  int ret = 0;
	  for (int i = 0; i < a.size(); i++) {
		  int ia = ((Integer)a.elementAt(i)).intValue();
		  int ib = ((Integer)b.elementAt(i)).intValue();
		  if (ia < ib) {
			  if (ret == 1)
				return 2;
			ret = -1;
		  } else if (ia > ib) {
			  if (ret == -1)
				return 2;
			ret = 1;
		  }
	  }
	  return ret;
  }

  // Add a field
  // Return false if there is a name conflict (among all fields only)
  public boolean addVar(String id, Type type) {
    if (fields.containsKey(id)) {
	  Variable tmp = getVar(id);
	  failLine = tmp.line;
	  failColumn = tmp.column;
	  dummyCnt += 1;
	  id = Integer.toString(dummyCnt);
	  fields.put(id, new Variable(id, type));
      return false;   
	}
      fields.put(id, new Variable(id, type));
      return true;
  }

  // Return a field with the specified name 
  public Variable getVar(String id) {
    if (containsVar(id)) 
      return (Variable)fields.get(id);
    else 
      return null;
  }
    
  public boolean containsVar(String id) {
    return fields.containsKey(id);
  }
    
	/*
  public boolean containsMethod(String id) {
    return methods.containsKey(id);
  }
  */
    
  public String parent() {
    return parent;
  }	    
} // Class

// Store all properties that describe a variable
class Variable {
	
  String id;
  Type type;
  int idRef;
  int line;
  int column;
    
  public Variable(String id, Type type) {
    this.id = id;
    this.type = type;
	Global.idRefCnt += 1;
	idRef = Global.idRefCnt;
	line = type.line; column = type.column;
  }
    
  public String id() { return id; }
    
  public Type type() { return type; }
	
} // Variable

// Store all properties that describe a variable
class Method {
	
  String id;  // Method name
  Type type;  // Return type
  Vector<Variable> params;          // Formal parameters
  Hashtable<String, Variable> vars; // Local variables
  int idRef;
  int failLine;
  int failColumn;
  public int dummyCnt;
  int line,column;
    
  public Method(String id, Type type, int aline ,int acolumn) {
    this.id = id;
    this.type = type;
	Global.idRefCnt += 1;
	idRef = Global.idRefCnt;
    params = new Vector<Variable>();
    vars = new Hashtable<String, Variable>();
    dummyCnt = 0;
	line = aline;
	column = acolumn;
  }
  
  public String toString() {
	  String ret = type.toString() + "(";
	  for (int i = 0; i < params.size(); i++) {
		  if (i > 0)
			ret += ", ";
		  ret += ((Variable)params.elementAt(i)).type.toString();
	  }
	  ret += ")";
	  return ret;
  }
  
  public boolean match(String qid, Vector<Type> vt) {
	  if (!qid.equals(id) || vt.size() != params.size()) return false;
	  for (int i = 0; i < vt.size(); i++) {
		  Type t1 = ((Variable)params.elementAt(i)).type;
		  Type t2 = (Type)vt.elementAt(i);
		  if (!t1.toString().equals(t2.toString())) return false;
	  }
	  return true;
  }
  
  public Vector<Integer> compatibility(String qid, Vector<Type> vt, SymbolTable st) {
	  if (!qid.equals(id) || vt.size() != params.size()) return null;
	  Vector< Integer > ret = new Vector< Integer >();
	  for (int i = 0; i < vt.size(); i++) {
		  Type t1 = ((Variable)params.elementAt(i)).type;
		  Type t2 = (Type)vt.elementAt(i);
		  if (t1.toString().equals(t2.toString())) ret.addElement(new Integer(0));
		  else if ((t1 instanceof DoubleType) && (t2 instanceof IntegerType)) ret.addElement(new Integer(1));
		  else if (!(t1 instanceof IdentifierType) && !(t1 instanceof InstanceType)) return null;
	      else if (!(t2 instanceof IdentifierType) && !(t2 instanceof InstanceType)) return null;
		  else {
			  int layer = 0;
			  Class tmp = st.getClass(t2.toString());
			  while(true) {
				  if (t1.toString().equals(tmp.id)) break;
				  else if (tmp.parent == null) return null;
				  else {
					  tmp = st.getClass(tmp.parent);
					  layer += 1;
				  }
			  }
			  ret.addElement(new Integer(layer));
		  }
	  }
	  return ret;
  }
    
  public String getId() { return id; }
    
  public Type type() { return type; }
    
  // Add a formal parameter
  // Return false if there is a name conflict
  public boolean addParam(String id, Type type) {
    // if (containsParam(id)) 
    if (vars.containsKey(id)) {
	  Variable tmp = getVar(id);
	  failLine = tmp.line;
	  failColumn = tmp.column;
	  dummyCnt += 1;
	  id = Integer.toString(dummyCnt);
	  params.addElement(new Variable(id, type));
	  vars.put(id, getParamAt(params.size() - 1));
      return false;   
	}
      params.addElement(new Variable(id, type));
	  vars.put(id, getParamAt(params.size() - 1));
      return true;
  }
    
  public Enumeration getParams(){
    return params.elements();
  }

  // Return a formal parameter by position (i=0 means 1st parameter)
  public Variable getParamAt(int i){
    if (i < params.size())
      return (Variable)params.elementAt(i);
    else
      return null;
  }

  // Add a local variable
  // Return false if there is a name conflict
  public boolean addVar(String id, Type type) {
    if (vars.containsKey(id)) {
	  Variable tmp = getVar(id);
	  failLine = tmp.line;
	  failColumn = tmp.column;
	  dummyCnt += 1;
	  id = Integer.toString(dummyCnt);
	  vars.put(id, new Variable(id, type));
      return false;   
	}
      vars.put(id, new Variable(id, type));
      return true;
  }
    
  public boolean containsVar(String id) {
    return vars.containsKey(id);
  }
    
  /*
  public boolean containsParam(String id) {
    for (int i = 0; i< params.size(); i++)
      if (((Variable)params.elementAt(i)).id.equals(id))
        return true;
    return false;
  }
  */
    
  public Variable getVar(String id) {
    if (containsVar(id)) 
      return (Variable)vars.get(id);
    else 
      return null;
  }

  /*
  // Return a formal parameter by name
  public Variable getParam(String id) {
    for (int i = 0; i< params.size(); i++)
      if (((Variable)params.elementAt(i)).id.equals(id))
        return (Variable)(params.elementAt(i));
	
      return null;
  }
  */
    
} // Method


