/*
 * Author: Zachary Tschirhart
 * Section: Thursday 2-3pm
 * Notes: I could not get some of the special cases to work out, but everything else
 * seems to work just fine. So, sqrt, expt and exp don't work when converting to 
 * different equations, everything else does. even the eval would work for special
 * cases.
 *
 *
 */


/**
 * this class Cons implements a Lisp-like Cons cell
 * 
 * @author  Gordon S. Novak Jr.
 * @version 29 Nov 01; 25 Aug 08; 05 Sep 08; 08 Sep 08; 12 Sep 08; 24 Sep 08
 *          06 Oct 08; 07 Oct 08; 09 Oct 08; 23 Oct 08; 27 Mar 09; 06 Aug 10
 */

public class Cons
{
    // instance variables
    private Object car;
    private Cons cdr;
    private Cons(Object first, Cons rest)
       { car = first;
         cdr = rest; }
    public static Cons cons(Object first, Cons rest)
      { return new Cons(first, rest); }
    public static boolean consp (Object x)
       { return ( (x != null) && (x instanceof Cons) ); }
// safe car, returns null if lst is null
    public static Object first(Cons lst) {
        return ( (lst == null) ? null : lst.car  ); }
// safe cdr, returns null if lst is null
    public static Cons rest(Cons lst) {
      return ( (lst == null) ? null : lst.cdr  ); }
    public static Object second (Cons x) { return first(rest(x)); }
    public static Object third (Cons x) { return first(rest(rest(x))); }
    public static void setfirst (Cons x, Object i) { x.car = i; }
    public static void setrest  (Cons x, Cons y) { x.cdr = y; }
   public static Cons list(Object ... elements) {
       Cons list = null;
       for (int i = elements.length-1; i >= 0; i--) {
           list = cons(elements[i], list);
       }
       return list;
   }
    /* access functions for expression representation */
    public static Object op  (Cons x) { return first(x); }
    public static Object lhs (Cons x) { return first(rest(x)); }
    public static Object rhs (Cons x) { return first(rest(rest(x))); }
    public static boolean numberp (Object x)
       { return ( (x != null) &&
                  (x instanceof Integer || x instanceof Double) ); }
    public static boolean integerp (Object x)
       { return ( (x != null) && (x instanceof Integer ) ); }
    public static boolean floatp (Object x)
       { return ( (x != null) && (x instanceof Double ) ); }
    public static boolean stringp (Object x)
       { return ( (x != null) && (x instanceof String ) ); }

/* convert a list to a string for printing */
    public String toString() {
       return ( "(" + toStringb(this) ); }
    public static String toString(Cons lst) {
       return ( "(" + toStringb(lst) ); }
    private static String toStringb(Cons lst) {
       return ( (lst == null) ?  ")"
                : ( first(lst) == null ? "()" : first(lst).toString() )
                  + ((rest(lst) == null) ? ")" 
                     : " " + toStringb(rest(lst)) ) ); }

    public boolean equals(Object other) { return equal(this,other); }

    // tree equality
public static boolean equal(Object tree, Object other) {
    if ( tree == other ) return true;
    if ( consp(tree) )
        return ( consp(other) &&
                 equal(first((Cons) tree), first((Cons) other)) &&
                 equal(rest((Cons) tree), rest((Cons) other)) );
    return eql(tree, other); }

    // simple equality test
public static boolean eql(Object tree, Object other) {
    return ( (tree == other) ||
             ( (tree != null) && (other != null) &&
               tree.equals(other) ) ); }

// member returns null if requested item not found
public static Cons member (Object item, Cons lst) {
  if ( lst == null )
     return null;
   else if ( item.equals(first(lst)) )
           return lst;
         else return member(item, rest(lst)); }

public static Cons union (Cons x, Cons y) {
  if ( x == null ) return y;
  if ( member(first(x), y) != null )
       return union(rest(x), y);
  else return cons(first(x), union(rest(x), y)); }

public static boolean subsetp (Cons x, Cons y) {
    return ( (x == null) ? true
             : ( ( member(first(x), y) != null ) &&
                 subsetp(rest(x), y) ) ); }

public static boolean setEqual (Cons x, Cons y) {
    return ( subsetp(x, y) && subsetp(y, x) ); }

    // combine two lists: (append '(a b) '(c d e))  =  (a b c d e)
public static Cons append (Cons x, Cons y) {
  if (x == null)
     return y;
   else return cons(first(x),
                    append(rest(x), y)); }

    // look up key in an association list
    // (assoc 'two '((one 1) (two 2) (three 3)))  =  (two 2)
public static Cons assoc(Object key, Cons lst) {
  if ( lst == null )
     return null;
  else if ( key.equals(first((Cons) first(lst))) )
      return ((Cons) first(lst));
          else return assoc(key, rest(lst)); }

    public static int square(int x) { return x*x; }
    public static int pow (int x, int n) {
        if ( n <= 0 ) return 1;
        if ( (n & 1) == 0 )
            return square( pow(x, n / 2) );
        else return x * pow(x, n - 1); }

public static Cons formulas = 
    list( list( "=", "s", list("*", new Double(0.5),
                               list("*", "a",
                                list("expt", "t", new Integer(2))))),
          list( "=", "s", list("+", "s0", list( "*", "v", "t"))),
          list( "=", "a", list("/", "f", "m")),
          list( "=", "v", list("*", "a", "t")),
          list( "=", "f", list("/", list("*", "m", "v"), "t")),
          list( "=", "f", list("/", list("*", "m",
                                         list("expt", "v", new Integer(2))),
                               "r")),
          list( "=", "h", list("-", "h0", list("*", new Double(4.94),
                                               list("expt", "t",
                                                    new Integer(2))))),
          list( "=", "c", list("sqrt", list("+",
                                            list("expt", "a",
                                                 new Integer(2)),
                                            list("expt", "b",
                                                 new Integer(2))))),
          list( "=", "v", list("*", "v0",
                               list("-", new Double(1.0),
                                    list("exp", list("/", list("-", "t"),
                                                     list("*", "r", "c"))))))
          );

    // Note: this list will handle most, but not all, cases.
    // The binary operators - and / have special cases.
public static Cons opposites = 
    list( list( "+", "-"), list( "-", "+"), list( "*", "/"),
          list( "/", "*"), list( "sqrt", "expt"), list( "expt", "sqrt"),
          list( "log", "exp"), list( "exp", "log"));

public static void printanswer(String str, Object answer) {
    System.out.println(str +
                       ((answer == null) ? "null" : answer.toString())); }

    // ****** your code starts here ******
public static Cons reverse (Cons lst){
   return reverseb (lst, null);
}
public static Cons reverseb (Cons lst, Cons lstb){
   return lst == null ? lstb : reverseb(rest(lst), cons(first(lst), lstb));
}

public static Cons findpath(Object item, Object cave) {
   return reverse(findpathb(item, cave, null));
}
public static Cons findpathb(Object item, Object cave, Cons result){
   if(consp(cave)){ 
      return append(findpathb(item, first((Cons)cave), cons("first", result)),
          findpathb(item, rest((Cons)cave), cons("rest", result)));
   }
   if(item.equals(cave)) return cons("done", result);
   else return null;
}

public static Object follow(Cons path, Object cave) {
   if(consp(cave) && first((Cons)path).equals("first")) 
      return follow(rest((Cons)path),first((Cons)cave));
   else if(consp(cave) && first((Cons)path).equals("rest"))
      return follow(rest((Cons)path), rest((Cons)cave));
   else if(first((Cons)path).equals("done"))
      return cave;
   else
      return null;
}

public static Object corresp(Object item, Object tree1, Object tree2) {
   return follow(findpath(item, tree1), tree2);
}

public static void setrhs(Cons e, Object item){
   setfirst(rest(rest(e)), item);
}
public static void setlhs(Cons e, Object item){
   setfirst(rest(e), item);
}

public static Cons reverseMath(Cons e){
   Object rightSide = rhs(e);
   Object leftSide = lhs(e);
   setrhs(e, rightSide);
   setlhs(e, leftSide);
   return e;
}

public static Cons solve(Cons e, String v) {
   return solveb(e, v, null);
}

public static Cons solveb(Cons e, String v, Cons stack){
   if(stack == null && lhs(e).equals(v)) return e;
   Cons result = null;
   if(rhs(e) == null && lhs(e).equals(v))
      result = list("=", v, solved(cons(list(op(e), lhs(e), "leftonly"), stack),v));
   else if(lhs(e).equals(v))
      result = list("=", v, solved(cons(list(op(e), rhs(e), "right"), stack), v));
   else if(rhs(e) != null && rhs(e).equals(v)) 
      result = list("=", v, solved(cons(list(op(e), lhs(e), "left"), stack), v));
   if(result != null)
      return result;

   if(rhs(e) == null && consp(lhs(e))){
      result = solveb((Cons)lhs(e), v, cons(list(op(e), lhs(e), "leftonly"), stack));
      if(result != null)
	 return result;
   }
   if(consp(lhs(e))){
      result = solveb((Cons)lhs(e), v, cons(list(op(e), rhs(e), "right"), stack));
      if(result != null)
	 return result;
   }
   if(consp(rhs(e))) {
      result = solveb((Cons)rhs(e), v, cons(list(op(e), lhs(e), "left"), stack));
      if(result != null)
	 return result;
   }
   return null;
}      

public static Cons solved(Cons e, String v){
   if(rest(rest(e)) == null){
      
      if(third((Cons)first(e)).equals("right")){
	 if(first((Cons)first(e)).equals("expt"))
	    return list(second((Cons)assoc(first((Cons)first(e)), opposites)), 
			second((Cons)first(rest(e))));
	 else
	    return list(second((Cons)assoc(first((Cons)first(e)), opposites)), 
			second((Cons)first(rest(e))), second((Cons)first(e)));
      }
      else if(third((Cons)first(e)).equals("leftonly")){
	 
	 if(first((Cons)first(e)).equals("sqrt"))
	    return list(second((Cons)assoc(first((Cons)first(e)), opposites)), 
			second((Cons)first(rest(e))), (Double) 2.0);
	 if(first((Cons)first(e)).equals("-"))
	    return list("-", second((Cons)first(rest(e))));
	 else
	    return list(second((Cons)assoc(first((Cons)first(e)), opposites)), 
			second((Cons)first(rest(e))));
      }
	 
      else if(third((Cons)first(e)).equals("left")){
	 if(first((Cons)first(e)).equals("expt"))
	    return list(second((Cons)assoc(first((Cons)first(e)), opposites)), 
			second((Cons)first(rest(e))));
	 else
	    return list(second((Cons)assoc(first((Cons)first(e)), opposites)), 
		     second((Cons)first(e)), second((Cons)first(rest(e))));
      }
   }
   if(third((Cons)first(e)).equals("right")) {
      if(first((Cons)first(e)).equals("expt"))
	 return list(second((Cons)assoc(first((Cons)first(e)), opposites)), 
	      solved(rest(e), v));
      else
	 return list(second((Cons)assoc(first((Cons)first(e)), opposites)), 
   		  solved(rest(e), v), second((Cons)first(e)));
   }
   else if(third((Cons)first(e)).equals("leftonly")){
      if(first((Cons)first(e)).equals("sqrt"))
	 return list(second((Cons)assoc(first((Cons)first(e)), opposites)), 
		     solved(rest(e),v), (Double) 2.0);
      else if(first((Cons)first(e)).equals("-"))
	 return list("-", solved(rest(e), v));
      else
	 return list(second((Cons)assoc(first((Cons)first(e)), opposites)), 
		     solved(rest(e),v));
   }

   else if(third((Cons)first(e)).equals("left")){
      if(first((Cons)first(e)).equals("expt"))
	 return list(second((Cons)assoc(first((Cons)first(e)), opposites)), 
		     solved(rest(e),v));
      else
	 return list(second((Cons)assoc(first((Cons)first(e)), opposites)), 
		     second((Cons)first(e)), solved(rest(e), v));
   }

   return null;
}

public static Double solveit (Cons equations, String var, Cons values) {
   Cons tempValues = values;
   Cons varList = cons(var, null);
   while(tempValues != null){
      varList = append(varList, cons(first((Cons)first(tempValues)),null));
      tempValues = rest(tempValues);
   }

   while(!setEqual(vars(first(equations)), varList))
      equations = rest(equations);

   return eval((Cons)rhs(solve((Cons)first(equations),var)), values);
}

 
    // Include your functions vars and eval from the previous assignment.
    // Modify eval as described in the assignment.
public static Cons vars (Object expr) {
  return varsb(expr, null);
}
public static Cons varsb (Object expr, Cons result){
  if(stringp(expr)) return cons(expr, result);
  if(numberp(expr)) return null;
  if(consp(expr) && rhs((Cons)expr) == null) return varsb(lhs((Cons)expr), result);
  return union(varsb(lhs((Cons)expr), result), varsb(rhs((Cons)expr), result));
}  

public static Double eval (Object tree, Cons bindings) {
  Double lhs;
  Double rhs;
  if(!consp(tree)) tree = cons(" ", cons(tree, null));

  if(consp(lhs((Cons)tree))) lhs = eval(lhs((Cons)tree), bindings);
  else if(stringp(lhs((Cons)tree))) lhs = (Double)first(rest(assoc(lhs((Cons)tree), bindings)));
  else lhs = (Double)lhs((Cons)tree);
   
  if(consp(rhs((Cons)tree)))
     rhs = eval(rhs((Cons)tree), bindings);
  else if(stringp(rhs((Cons)tree)))
     rhs = (Double)first(rest(assoc(rhs((Cons)tree), bindings)));
  else if(rhs((Cons)tree) == null && op((Cons)tree).equals(" ")) 
     return lhs;
  else if(rhs((Cons)tree) == null) rhs = null;
  else if(floatp(rhs((Cons)tree))) rhs = (Double)rhs((Cons)tree);
  else rhs = ((Integer)rhs((Cons)tree)).doubleValue();
  
  
  if(op((Cons)tree).equals("+")) return lhs + rhs;
  if(op((Cons)tree).equals("-")){
     if (rhs((Cons)tree) == null) return -1*lhs;
     else return lhs - rhs;
  }
  if(op((Cons)tree).equals("*")) return lhs * rhs;
  if(op((Cons)tree).equals("/")) return lhs / rhs;
  if(op((Cons)tree).equals("expt")) return Math.pow(lhs, rhs);
  if(op((Cons)tree).equals("sqrt")) return Math.sqrt(lhs);
  if(op((Cons)tree).equals("log")) return Math.log(lhs);
  if(op((Cons)tree).equals("exp")) return Math.exp(lhs);
  return null;
}


    // ****** your code ends here ******

    public static void main( String[] args ) {

        Cons cave = list("rocks", "gold", list("monster"));
        Cons path = findpath("gold", cave);
        printanswer("cave = " , cave);
        printanswer("path = " , path);
        printanswer("follow = " , follow(path, cave));
        Cons treea = list(list("my", "eyes"),
                          list("have", "seen", list("the", "light")));
        Cons treeb = list(list("my", "ears"),
                          list("have", "heard", list("the", "music")));
        printanswer("treea = " , treea);
        printanswer("treeb = " , treeb);
        printanswer("corresp = " , corresp("light", treea, treeb));
        System.out.println("formulas = ");
        Cons frm = formulas;
        Cons vset = null;
        while ( frm != null ) {
           printanswer("   "  , ((Cons)first(frm)));
	   vset = vars((Cons)first(frm));
	   while ( vset != null ) {
	      printanswer("       "  ,
			  solve((Cons)first(frm), (String)first(vset)) );
	      vset = rest(vset); 
	   }
	   frm = rest(frm); 
	}

        Cons bindings = list( list("a", (Double) 32.0),
                              list("t", (Double) 4.0));
        printanswer("Eval:      " , rhs((Cons)first(formulas)));
        printanswer("  bindings " , bindings);
        printanswer("  result = " , eval(rhs((Cons)first(formulas)), bindings));

        printanswer("Tower: " , solveit(formulas, "h0",
                                            list(list("h", new Double(0.0)),
                                                 list("t", new Double(4.0)))));

        printanswer("Car: " , solveit(formulas, "a",
                                            list(list("v", new Double(88.0)),
                                                 list("t", new Double(8.0)))));
        
        printanswer("Capacitor: " , solveit(formulas, "c",
                                            list(list("v", new Double(3.0)),
                                                 list("v0", new Double(6.0)),
                                                 list("r", new Double(10000.0)),
                                                 list("t", new Double(5.0)))));

        printanswer("Ladder: " , solveit(formulas, "b",
                                            list(list("a", new Double(6.0)),
                                                 list("c", new Double(10.0)))));


      }

}
