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

/** Szablon interfejsu dla rozkazów.
 *  który zawiera funkcjonalność do udostępnienia.
 *  Parametr T powinien być konkretyzowany interfejsem opisującym udostępnianą przez Przyjmującego rozkaz funkcjonalnoc.
 *  Małe przypomnienie: w Javie szablony działają inaczej niż w C++
 * @author Maciej Majewski
 */
public interface Order<T> {
    public void execute(T subject);
}
