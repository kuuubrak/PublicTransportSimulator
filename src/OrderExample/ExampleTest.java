/*
 * Copyright (C) 2014 Maciej Majewski
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package OrderExample;

/** Test funkcjonalności.
 *  Rozkazy mogą przychodzić przez net rzecz jasna, czy skądkolwiek indziej.
 * @author Maciej Majewski
 */
public class ExampleTest {
    public static void main(String... args){
        ExampleOrderRecipient soldier=new ExampleOrderRecipient();
        
        /* Przychodzące rozkazy wystarczy jedynie zrzutować na typ surowy (postuluję
           obecność na łączu jedynie takich rozkazów) z przychodzącego po ObjectStream'ie
           obiektu. Poniżej symulacja przychodzących właśnie w taki sposób rozkazów
           (nie obchodzą nas konkretne typy).
        */
        Order uno=new ExampleOrder_Trivial(); //surowy typ
        Order due=new ExampleOrder_WithArgument("Hende hoch!"); //surowy typ
        Order tre=new ExampleOrder_Evil(); //surowy typ
        
        /* Próba wykonania. W faktycznej implementacji serwer nie powinien
           przysyłać do modułu rozkazów nie przeznaczonych dla niego, więc
           ponąć będzie można blok try.
        */
        try{
            soldier.executeOrder(uno);
            soldier.executeOrder(due);
            soldier.executeOrder(tre);
        }catch(ClassCastException ex){
            System.out.println("Me not understandz. Taht ordah be bad.");
        }
    }
}
