package org.eclipse.cargotracker.interfaces.booking.facade.internal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.eclipse.cargotracker.application.BookingService;
import org.eclipse.cargotracker.domain.model.cargo.Cargo;
import org.eclipse.cargotracker.domain.model.cargo.CargoRepository;
import org.eclipse.cargotracker.domain.model.cargo.Itinerary;
import org.eclipse.cargotracker.domain.model.cargo.TrackingId;
import org.eclipse.cargotracker.domain.model.handling.HandlingEvent;
import org.eclipse.cargotracker.domain.model.handling.HandlingEventRepository;
import org.eclipse.cargotracker.domain.model.location.Location;
import org.eclipse.cargotracker.domain.model.location.LocationRepository;
import org.eclipse.cargotracker.domain.model.location.UnLocode;
import org.eclipse.cargotracker.domain.model.voyage.VoyageRepository;
import org.eclipse.cargotracker.interfaces.booking.facade.BookingServiceFacade;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.CargoRoute;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.CargoTracking;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.RouteCandidate;
import org.eclipse.cargotracker.interfaces.booking.facade.internal.assembler.CargoRouteDtoAssembler;
import org.eclipse.cargotracker.interfaces.booking.facade.internal.assembler.CargoTrackingDtoAssembler;
import org.eclipse.cargotracker.interfaces.booking.facade.internal.assembler.ItineraryCandidateDtoAssembler;
import org.eclipse.cargotracker.interfaces.booking.facade.internal.assembler.LocationDtoAssembler;
import org.eclipse.cargotracker.interfaces.tracking.web.CargoTrackingViewAdapter;

@ApplicationScoped
public class DefaultBookingServiceFacade implements BookingServiceFacade, Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private BookingService bookingService;
	@Inject
	private LocationRepository locationRepository;
	@Inject
	private CargoRepository cargoRepository;
	@Inject
	private VoyageRepository voyageRepository;
	@Inject
	private HandlingEventRepository handlingEventRepository;

	@Override
	public List<org.eclipse.cargotracker.interfaces.booking.facade.dto.Location> listShippingLocations() {
		List<Location> allLocations = locationRepository.findAll();
		LocationDtoAssembler assembler = new LocationDtoAssembler();
		return assembler.toDtoList(allLocations);
	}

	@Override
	public String bookNewCargo(String origin, String destination, Date arrivalDeadline) {
		TrackingId trackingId = bookingService.bookNewCargo(new UnLocode(origin), new UnLocode(destination),
				arrivalDeadline);
		return trackingId.getIdString();
	}

	@Override
	public CargoRoute loadCargoForRouting(String trackingId) {
		Cargo cargo = cargoRepository.find(new TrackingId(trackingId));
		CargoRouteDtoAssembler assembler = new CargoRouteDtoAssembler();
		return assembler.toDto(cargo);
	}

	@Override
	public void assignCargoToRoute(String trackingIdStr, RouteCandidate routeCandidateDTO) {
		Itinerary itinerary = new ItineraryCandidateDtoAssembler().fromDTO(routeCandidateDTO, voyageRepository,
				locationRepository);
		TrackingId trackingId = new TrackingId(trackingIdStr);

		bookingService.assignCargoToRoute(itinerary, trackingId);
	}

	@Override
	public void changeDestination(String trackingId, String destinationUnLocode) {
		bookingService.changeDestination(new TrackingId(trackingId), new UnLocode(destinationUnLocode));
	}

	@Override
	public void changeDeadline(String trackingId, Date arrivalDeadline) {
		bookingService.changeDeadline(new TrackingId(trackingId), arrivalDeadline);
	}

	@Override
	public List<CargoRoute> listAllCargos() {
		List<Cargo> cargos = cargoRepository.findAll();
		List<CargoRoute> routes = new ArrayList<>(cargos.size());

		CargoRouteDtoAssembler assembler = new CargoRouteDtoAssembler();

		for (Cargo cargo : cargos) {
			routes.add(assembler.toDto(cargo));
		}

		return routes;
	}

	@Override
	public List<TrackingId> listAllTrackingIds() {
		return cargoRepository.getAllTrackingIds();
	}

	@Override
	public CargoTracking loadCargoForTracking(String trackingId) {
		TrackingId tid = new TrackingId(trackingId);
		Cargo cargo = cargoRepository.find(tid);

		if (cargo == null) {
			return null;
		}

		CargoTrackingDtoAssembler assembler = new CargoTrackingDtoAssembler();

		List<HandlingEvent> handlingEvents = handlingEventRepository
				.lookupHandlingHistoryOfCargo(tid).getDistinctEventsByCompletionTime();

		return assembler.toDto(tid, cargo, handlingEvents);
	}

	@Override
	public List<RouteCandidate> requestPossibleRoutesForCargo(String trackingId) {
		List<Itinerary> itineraries = bookingService.requestPossibleRoutesForCargo(new TrackingId(trackingId));

		List<RouteCandidate> routeCandidates = new ArrayList<>(itineraries.size());
		ItineraryCandidateDtoAssembler dtoAssembler = new ItineraryCandidateDtoAssembler();
		for (Itinerary itinerary : itineraries) {
			routeCandidates.add(dtoAssembler.toDTO(itinerary));
		}

		return routeCandidates;
	}
}
