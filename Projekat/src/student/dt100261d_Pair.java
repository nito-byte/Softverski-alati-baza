/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import static java.awt.PageAttributes.MediaType.A;
import static java.awt.PageAttributes.MediaType.B;
import operations.PackageOperations.Pair;


public class dt100261d_Pair<A,B> implements Pair<A, B>{
    private A a; //first member of pair
    private B b; //second member of pair

    public dt100261d_Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }
    
    

    @Override
    public A getFirstParam() {
        return this.a;
    }

    @Override
    public B getSecondParam() {
        return this.b;
    }
    
}
