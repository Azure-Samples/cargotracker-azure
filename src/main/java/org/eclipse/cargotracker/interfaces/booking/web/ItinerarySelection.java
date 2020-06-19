package org.eclipse.cargotracker.interfaces.booking.web;

import org.eclipse.cargotracker.interfaces.booking.facade.BookingServiceFacade;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.CargoRoute;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.RouteCandidate;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Handles itinerary selection. Operates against a dedicated service facade, and
 * could easily be rewritten as a thick Swing client. Completely separated from
 * the domain layer, unlike the tracking user interface.
 * <p/>
 * In order to successfully keep the domain model shielded from user interface
 * considerations, this approach is generally preferred to the one taken in the
 * tracking controller. However, there is never any one perfect solution for all
 * situations, so we've chosen to demonstrate two polarized ways to build user
 * interfaces.
 *
 */
@Named
@ViewScoped
public class ItinerarySelection implements Serializable {

    private static final long serialVersionUID = 1L;
    private String trackingId;
    private CargoRoute cargo;
    List<RouteCandidate> routeCandidates;
    @Inject
    private BookingServiceFacade bookingServiceFacade;

    public List<RouteCandidate> getRouteCandidates() {
        return routeCandidates;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public CargoRoute getCargo() {
        return cargo;
    }

    public List<RouteCandidate> getRouteCanditates() {
        return routeCandidates;
    }

    public void load() {
        cargo = bookingServiceFacade.loadCargoForRouting(trackingId);
        routeCandidates = bookingServiceFacade
                .requestPossibleRoutesForCargo(trackingId);
    }

    public String assignItinerary(int routeIndex) {
        RouteCandidate route = routeCandidates.get(routeIndex);
        bookingServiceFacade.assignCargoToRoute(trackingId, route);

        return "show.html?faces-redirect=true&trackingId=" + trackingId;
    }
}
