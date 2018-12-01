package nodomain.chuck.loadmanager.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Set;

/**
 * Derived from SteVe project
 *
 * @author Chuck Harrison <cfharr@gmail.com>
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 *
 */

// respond to emoncms-style fulljson data posting, LoadManager/input/post?node=xyz&fulljson={"ampL1-N":15.2}
@Controller
@RequestMapping(value = "/input")
public class InputController {
    @RequestMapping(value = "/post", method = RequestMethod.GET)
    @ResponseBody
    public String getInput(@RequestParam(value="node") String node,
                               @RequestParam(value="time", required=false) Long time,
                               @RequestParam(value="fulljson", required=false) String json) {
        Map<String, Float> valuesMap;
        if (json != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                valuesMap = objectMapper.readValue(json, new TypeReference<Map<String,Float>>(){});
            }
            catch (IOException e) {
                return e.getMessage();
            }
        }
        if (time == null) {
            time = System.currentTimeMillis()/1000L;
        }
        String rv = "node " + node + "<br>time " + time +"<br>";
        Set< Map.Entry< String,Float> > st = valuesMap.entrySet();    
        for (Map.Entry< String,Float> me:st) 
        { 
            rv += me.getKey()+": " + me.getValue() + "<br>"; 
        } 
        return rv;
    }
}


