package nodomain.chuck.loadmanager.web.controller;

/*
import de.rwth.idsg.steve.repository.ChargePointRepository;
import de.rwth.idsg.steve.repository.dto.ConnectorStatus;
import de.rwth.idsg.steve.service.ChargePointHelperService;
import de.rwth.idsg.steve.utils.ConnectorStatusCountFilter;
import de.rwth.idsg.steve.utils.ConnectorStatusFilter;
import de.rwth.idsg.steve.web.dto.ConnectorStatusForm;
*/
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 *
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 *
 */
@Controller
@RequestMapping(value = "/manager", method = RequestMethod.GET)
public class HomeController {

 //   @Autowired private ChargePointRepository chargePointRepository;
 //   @Autowired private ChargePointHelperService chargePointHelperService;

    private static final String PARAMS = "params";

    // -------------------------------------------------------------------------
    // Paths
    // -------------------------------------------------------------------------

    private static final String HOME_PREFIX = "/home";

    private static final String OCPP_JSON_STATUS = HOME_PREFIX + "/ocppJsonStatus";
    private static final String CONNECTOR_STATUS_PATH = HOME_PREFIX + "/connectorStatus";
    private static final String CONNECTOR_STATUS_QUERY_PATH = HOME_PREFIX + "/connectorStatus/query";
    // -------------------------------------------------------------------------
    // HTTP methods
    // -------------------------------------------------------------------------

    @RequestMapping(value = {"", HOME_PREFIX})
    public String getHome(Model model) {
 //       model.addAttribute("stats", chargePointHelperService.getStats());
        return "home";
    }

/*
    @RequestMapping(value = CONNECTOR_STATUS_PATH)
    public String getConnectorStatus(Model model) {
        return getConnectorStatusQuery(new ConnectorStatusForm(), model);
    }

    @RequestMapping(value = CONNECTOR_STATUS_QUERY_PATH)
    public String getConnectorStatusQuery(@ModelAttribute(PARAMS) ConnectorStatusForm params, Model model) {
        model.addAttribute("cpList", chargePointRepository.getChargeBoxIds());
        model.addAttribute("statusValues", ConnectorStatusCountFilter.ALL_STATUS_VALUES);
        model.addAttribute(PARAMS, params);

        List<ConnectorStatus> latestList = chargePointRepository.getChargePointConnectorStatus(params);
        List<ConnectorStatus> filteredList = ConnectorStatusFilter.filterAndPreferZero(latestList);
        model.addAttribute("connectorStatusList", filteredList);
        return "connectorStatus";
    }

    @RequestMapping(value = OCPP_JSON_STATUS)
    public String getOcppJsonStatus(Model model) {
        model.addAttribute("ocppJsonStatusList", chargePointHelperService.getOcppJsonStatus());
        return "ocppJsonStatus";
    }
*/
}
