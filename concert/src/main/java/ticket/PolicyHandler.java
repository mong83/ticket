package ticket;

import ticket.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PolicyHandler{
    @Autowired ConcertRepository concertRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverBookingCancelled_ModifyStock(@Payload BookingCancelled bookingCancelled){

        if(!bookingCancelled.validate()) return;

        System.out.println("\n\n##### listener ModifyStock : " + bookingCancelled.toJson() + "\n\n");

        // BIZ로직 start //
        Optional<Concert> concert = concertRepository.findById(bookingCancelled.getCcId()); 



        if(concert.isPresent()){

                Concert concertValue = concert.get();
                
                System.out.println("\n\n stock: " + concertValue.getStock() + "\n\n"+" ccId: " + concertValue.getCcId() + "\n\n");

                concertValue.setStock(concertValue.getStock() - bookingCancelled.getQty()); 
                System.out.println("\n\n change  stock: " + concertValue.getStock() + "\n\n");
                concertRepository.save(concertValue);
                                        
                
        }
        
        // BIZ로직 end //
        
        /* sample 주석처리
        Concert concert = new Concert();
        concertRepository.save(concert);
        */    
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString){}


}
