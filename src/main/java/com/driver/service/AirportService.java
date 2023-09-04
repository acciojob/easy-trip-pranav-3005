package com.driver.service;

import com.driver.Repository.AirportRepository;
import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class AirportService {

    @Autowired
    AirportRepository repository=new AirportRepository();
    public void addAirport(Airport airport) {
        repository.addAirport(airport.getAirportName(),airport);
    }

    public String getLargestAirportName() {
        return repository.getLargestAirportName();
    }

    public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity, City toCity) {
        return repository.getShortestDurationOfPossibleBetweenTwoCities(fromCity, toCity);
    }

    public String addFlight(Flight flight) {
        repository.addFlight(flight);
        return "SUCCESS";
    }

    public String addPassenger(Passenger passenger) {
        repository.addPassenger(passenger);
        return "SUCCESS";
    }

    public String bookATicket(Integer flightId, Integer passengerId) {
        HashMap<Integer,ArrayList<Integer>> flightTicketsBooked = repository.getFlightTicketsBooked();
        HashMap<Integer,Flight> flights =repository.getFlights();//to get flight's maxcapacity
        HashMap<Integer,ArrayList<Integer>> usersBooking =repository.getUsersBooking();

        ArrayList<Integer> bookedList =flightTicketsBooked.getOrDefault(flightId,new ArrayList<>());

        int noOfBooked=bookedList.size();
        int fcapacity=flights.get(flightId).getMaxCapacity();

        if( !flights.containsKey(flightId) ||  noOfBooked>=fcapacity || (usersBooking.containsKey(passengerId) && usersBooking.get(passengerId).contains(flightId) ) )
        {

            return "FAILURE";
        }

        //......
        if(noOfBooked==0)
        {
            flightTicketsBooked.put(flightId,new ArrayList<>());
        }
        if(!usersBooking.containsKey(passengerId) )
        {
            usersBooking.put(passengerId,new ArrayList<>());
        }

        flightTicketsBooked.get(flightId).add(passengerId);
        usersBooking.get(passengerId).add((flightId));

        return "SUCCESS";



    }

    public String getAirportNameFromFlightId(Integer flightId) {

        HashMap<Integer,Flight> flights =repository.getFlights();
        HashMap<String , Airport> airports= repository.getairports();

        if(!flights.containsKey(flightId))
            return null;

        City fromCity= flights.get(flightId).getFromCity();

        for(String i: airports.keySet())
        {
            City airportCity=airports.get(i).getCity();
            if( fromCity.equals(airportCity) )
            {
                return airports.get(i).getAirportName();
            }
        }

        return null;
    }

    public String cancelATicket(Integer flightId, Integer passengerId) {

        //If the passenger has not booked a ticket for that flight or the flightId is invalid or in any other failure case
        // then return a "FAILURE" message
        // Otherwise return a "SUCCESS" message
        // and also cancel the ticket that passenger had booked earlier on the given flightId

        HashMap<Integer,ArrayList<Integer>> usersBooking= repository.getUsersBooking();
        HashMap<Integer,ArrayList<Integer>> flightTicketsBooked= repository.getFlightTicketsBooked();

        if( !(flightTicketsBooked.containsKey(flightId)) || !(usersBooking.containsKey(passengerId)) || !( usersBooking.get(passengerId).contains(flightId) ) )
        {
            return "FAILURE";
        }

        flightTicketsBooked.get(flightId).remove(passengerId);
        usersBooking.get(passengerId).remove((flightId));

        return "SUCCESS";
    }

    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId) {
        HashMap<Integer,ArrayList<Integer>> usersBooking = repository.getUsersBooking();
        if(usersBooking.containsKey(passengerId))
            return usersBooking.get(passengerId).size();
        return 0;
    }

    public int calculateFlightFare(Integer flightId) {
        //Calculation of flight prices is a function of number of people who have booked the flight already.
        //Price for any flight will be : 3000 + noOfPeopleWhoHaveAlreadyBooked*50
        //Suppose if 2 people have booked the flight already : the price of flight for the third person will be 3000 + 2*50 = 3100
        //This will not include the current person who is trying to book, he might also be just checking price

        HashMap<Integer,ArrayList<Integer>> flightTicketsBooked= repository.getFlightTicketsBooked();

        //1st ticket of that flight (none booked the flight)
        if(!flightTicketsBooked.containsKey(flightId) )
        {
            return 3000;
        }

        int count=flightTicketsBooked.get(flightId).size();

        return 3000 + (count*50);


    }

    public int calculateRevenueOfAFlight(Integer flightId) {
        HashMap<Integer,ArrayList<Integer>> flightTicketsBooked= repository.getFlightTicketsBooked();

        int revenue=0;
        //none booked
        if( !flightTicketsBooked.containsKey(flightId) )
            return revenue;

        int size=flightTicketsBooked.get(flightId).size();

        for(int i=0;i<size;i++)
        {
            revenue+= (3000 + (i*50) ); //adding actual cost of every passengers of flight
        }

        return revenue;
    }

    //null pointer error
    public int getNumberOfPeopleOn(Date date, String airportName) {
        HashMap<String,Airport> airports = repository.getairports();
        HashMap<Integer,Flight> flights= repository.getFlights();
        HashMap<Integer,ArrayList<Integer>> flightTicketsBooked = repository.getFlightTicketsBooked();

        int count=0;

        for(Integer i:flights.keySet())
        {
            Date fDate=flights.get(i).getFlightDate();

            if( fDate.equals(date) )
            {
                if( flights.get(i).getFromCity().equals(airports.get(airportName).getCity()) || flights.get(i).getToCity().equals(airports.get(airportName).getCity()) )
                {
                    if(flightTicketsBooked.containsKey(i))
                    {
                        count+=flightTicketsBooked.get(i).size();
                    }
                }
            }
        }
        return count;
    }
}