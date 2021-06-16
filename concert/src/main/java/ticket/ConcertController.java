package ticket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Optional;

 @RestController
 public class ConcertController {


        @Autowired
        ConcertRepository concertRepository;

        @RequestMapping(value = "/checkAndBookStock",
                method = RequestMethod.GET,
                produces = "application/json;charset=UTF-8")

        public boolean checkAndBookStock(HttpServletRequest request, HttpServletResponse response)
                throws Exception {

                        System.out.println("##### /concert/checkAndBookStock  called #####");

                        boolean status = false;
                        
                        Long ccId = Long.valueOf(request.getParameter("ccId"));
                        int qty = Integer.parseInt(request.getParameter("qty"));
        
                        System.out.println("##### ccid #####" + ccId +"#########" + qty);
                        Optional<Concert> concert = concertRepository.findById(ccId);
                        
                        if(concert.isPresent()){

                                Concert concertValue = concert.get();

                                if (concertValue.getStock() >= qty) {
                                        concertValue.setStock(concertValue.getStock() - qty);
                                        concertRepository.save(concertValue);
                                        status = true;
                                }
                
                                
                        }

                        return status;
        
                

                }
 }
