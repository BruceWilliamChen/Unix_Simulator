package cs131.pa2.CarsTunnels;

import cs131.pa2.Abstract.*;
import java.util.*;
import cs131.pa2.Abstract.Log.*;
import java.util.concurrent.locks.*;

public class PriorityScheduler extends Tunnel{
	Collection<Tunnel> listOfTunnel;
	//key -> priority, value -> num of vehicles having that priority
	TreeMap<Integer, Integer> priorityKeeper;
	//a map to keep a track of which vehicle goes to which tunnel
	Map<Vehicle, BasicTunnel> vehicleToTunnel;
	Lock lock;
	Condition priorityCondition, enterTunnelCondition;
	
	
	public PriorityScheduler(String name, Collection<Tunnel> tunnels, Log log) {
		super(name, log);
		this.listOfTunnel = tunnels;
		this.priorityKeeper = new TreeMap<Integer, Integer>();
		this.vehicleToTunnel = new HashMap<Vehicle, BasicTunnel>();
		this.lock = new ReentrantLock();
		this.priorityCondition = lock.newCondition();
		this.enterTunnelCondition = lock.newCondition();
	}

	@Override
	public boolean tryToEnterInner(Vehicle vehicle) {
		lock.lock();
		int priority = vehicle.getPriority();
		try {
			//put the curr priority to the map
			priorityKeeper.put(priority, priorityKeeper.getOrDefault(priority, 0) + 1);
			//assume that tunnel available is null at this moment
			BasicTunnel tunnel = null;
			while (tunnel == null) {
				tunnel = tunnelCanEnter(vehicle);
				//then check the tunnel is something not null now
				if (tunnel == null) enterTunnelCondition.await();
				//then consider the first condition, priority
				while (priority < priorityKeeper.lastKey()) {
					priorityCondition.await();
				}
			}
			//now the vehicle is ready to go in
			//try to see if removed the pair or just decrement
			if (priorityKeeper.get(priority) > 1) {
				priorityKeeper.put(priority, priorityKeeper.get(priority) - 1);
			} else {
				//when the highest priority gets removed, that means other vehicles can be activated
				priorityKeeper.remove(priority);
				priorityCondition.signalAll();
			}
			this.vehicleToTunnel.put(vehicle, tunnel);
			tunnel.tryToEnterInner(vehicle);
			return true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		return false;
	}
	
	/*
	 * param, vehicle
	 * return, a basic tunnel that can be possibly entered
	 */
	public BasicTunnel tunnelCanEnter(Vehicle vehicle) {
		for (Tunnel tunnel: listOfTunnel) {
			BasicTunnel bt = (BasicTunnel) tunnel;
			if (bt.ifPossibleEnter(vehicle)) return bt;
		}
		return null;
	}

	@Override
	public void exitTunnelInner(Vehicle vehicle) {
		this.lock.lock();
		try {
			BasicTunnel bt = this.vehicleToTunnel.get(vehicle);
			bt.exitTunnelInner(vehicle);
			enterTunnelCondition.signalAll();
			this.vehicleToTunnel.remove(vehicle);
		} finally {
			this.lock.unlock();
		}
	}
}
