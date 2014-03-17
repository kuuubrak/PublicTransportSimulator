package network;

import java.io.Serializable;
import java.util.List;

public class Message implements Serializable
{
	private static final long serialVersionUID = 1L;
	/** Mozliwe moduly docelowe dla pakietu */
	public enum DESTINATIONS { GUI, PASSENGERS_MODULE, MANAGEMENT_MODULE }
	/** Modul docelowy pakietu */
	private final DESTINATIONS destination;
	/** Lista przesylanych obiektow */
	private final List<Object> objectsList;
	
	public Message(final DESTINATIONS destination, final List<Object> objectsList)
	{
		this.destination = destination;
		this.objectsList = objectsList;
	}
	
	public DESTINATIONS getDestination()
	{
		return destination;
	}
	
	public List<Object> getObjectsList()
	{
		return objectsList;
	}
}
