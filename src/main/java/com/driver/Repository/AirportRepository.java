package com.driver.Repository;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;

@Repository
public class AirportRepository {
    HashMap<String, Airport> airports =new HashMap<>();
    HashMap<Integer, Flight> flights=new HashMap<>();
    HashMap<Integer, Passenger> passengers=new HashMap<>();
    HashMap<Integer, ArrayList<Integer>> flightTicketsBooked = new HashMap<>(); //<flight id, its passengers id list>
    HashMap<Integer,ArrayList<Integer> > usersBooking=new HashMap<>(); //<passengerid , list of booked flight ids>
    public HashMap<Integer, ArrayList<Integer>> getFlightTicketsBooked()
    {
        return flightTicketsBooked;
    }
    public HashMap<Integer,ArrayList<Integer>> getUsersBooking()
    {
        return usersBooking;
    }
    public HashMap<Integer, Flight> getFlights() {
        return flights;
    }
    public HashMap<String,Airport> getairports()
    {
        return airports;
    }

    public void addAirport(String airportName, Airport airport) {
        airports.put(airportName,airport);
    }


    public String getLargestAirportName() {
        int maxt=0; //maxterminal
        String maxn=""; //max airport name

        for(String i: airports.keySet())
        {
            int curt= airports.get(i).getNoOfTerminals();
            if(curt > maxt)
            {
                maxt=curt;
                maxn=i;
            }
            else if(curt==maxt)
            {
                if(maxn.compareTo(i)<0)
                    maxn=i;
            }
        }

        return maxn;
    }

    public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity, City toCity) {
        double mind=Double.MAX_VALUE;

        for(int i: flights.keySet())
        {
            if(flights.get(i).getFromCity()==fromCity && flights.get(i).getToCity()==toCity)
            {
                mind=Math.min(mind,flights.get(i).getDuration());
            }
        }
        if(mind==Double.MAX_VALUE)
            return -1;

        return mind;
    }

    public void addFlight(Flight flight) {
        flights.put(flight.getFlightId(),flight);
    }

    public void addPassenger(Passenger passenger) {
        passengers.put(passenger.getPassengerId(), passenger);
    }


}