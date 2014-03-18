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

/** Przykładowa klasa wykonująca przychodzące rozkazy.
 *  Przyuważcie implementację znanej funkcjonalności/interfejsu
 * @author Maciej Majewski
 */
public class ExampleOrderRecipient implements ExampleFunctionality,
                                              OrderRecipient<ExampleFunctionality> {

    @Override
    public void printCrap() {
        System.out.println("Oh Crap!");
    }

    @Override
    public void printTxt(String toPrint) {
        System.out.println(toPrint);
    }

    @Override
    public void executeOrder(Order<ExampleFunctionality> toExec) {
        toExec.execute(this);
    }
    
    
}
