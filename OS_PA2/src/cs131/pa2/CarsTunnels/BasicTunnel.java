package cs131.pa2.CarsTunnels;

import cs131.pa2.Abstract.*;
import java.util.*;

public class BasicTunnel extends Tunnel{
	protected int numOfSled;
	protected int numOfCar;
	protected List<Direction> currDirection;
	
	public BasicTunnel(String name) {
		super(name);
		// TODO Auto-generated constructor stub
		this.numOfSled = 0;
		this.numOfCar = 0;
		//to keep a track of current vehicles in the tunnel
		this.currDirection = new ArrayList<Direction>();
	}

	@Override
	public synchronized boolean tryToEnterInner(Vehicle vehicle) {
		// TODO Auto-generated method stub
		//if vehicle is a car then the current tunnel must not have sled
		if (vehicle instanceof Car) {
			if (numOfSled > 0 || numOfCar >= 3) return false;
			//and also all cars have to have the same direction
			if (this.numOfCar == 0) {
				this.numOfCar++;
				//then add its direction to the list
				this.currDirection.add(vehicle.getDirection());
				return true;
			} else {
				//this means need to consider whatever already contains in the set
				if (!this.currDirection.contains(vehicle.getDirection())) return false;
				this.numOfCar++;
				this.currDirection.add(vehicle.getDirection());
				return true;
			}
		}
		if (vehicle instanceof Sled) {
			if (numOfSled >= 1 || numOfCar > 0) return false;
			this.numOfSled++;
			return true;
		}
		return false;
	}
	
	public synchronized boolean ifPossibleEnter(Vehicle vehicle) {
		if (vehicle instanceof Car) {
			if (numOfSled > 0 || numOfCar >= 3) return false;
			return true;
		}
		if (vehicle instanceof Sled) {
			if (numOfSled >= 1 || numOfCar > 0) return false;
			return true;
		}
		return false;
	}

	@Override
	public synchronized void exitTunnelInner(Vehicle vehicle) {
		// TODO Auto-generated method stub
		//here just remove the vehicle from the tunnel
		if (vehicle instanceof Sled) this.numOfSled--;
		if (vehicle instanceof Car) {
			this.numOfCar--;
			//then remove its direction from the list
			this.currDirection.remove(vehicle.getDirection());
		}
	}
	
}
