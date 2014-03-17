package network;

import java.util.List;

import DataModel.Bus;
import DataModel.BusStop;

public class Mockup 
{
	private final List<Bus> buses;
	private final List<BusStop> busStops;
	
	public Mockup(final List<Bus> buses, final List<BusStop> busStops)
	{
		this.buses = buses;
		this.busStops = busStops;
	}
	
	public List<Bus> getBuses()
	{
		return buses;
	}
	
	public List<BusStop> getBusStops()
	{
		return busStops;
	}
}
