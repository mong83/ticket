package ticket;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.Date;

@Entity
@Table(name="Booking_table")
public class Booking {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long bookingId;
    private Long ccId;
    private String ccName;
    private Integer qty;
    private Long customerId;
    private String bookingStatus;

    @PostPersist
    public void onPostPersist(){
       
        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.
        
        // 주석처리
        // ticket.external.Concert concert = new ticket.external.Concert(); 
        // mappings goes here
    
        /* REQ/RES로직 start*/
        boolean rslt = BookingApplication.applicationContext.getBean(ticket.external.ConcertService.class)
            .checkAndBookStock(this.getCcId(), this.getQty());

            if (rslt) {
            Booked booked = new Booked();
            BeanUtils.copyProperties(this, booked);
            booked.publishAfterCommit();
            }
        
       /*  로직 end */

  
        
    }

    @PreUpdate
    public void onPreUpdate(){
        BookingCancelled bookingCancelled = new BookingCancelled();
        BeanUtils.copyProperties(this, bookingCancelled);
        bookingCancelled.publishAfterCommit();

    }


    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }
    public Long getCcId() {
        return ccId;
    }

    public void setCcId(Long ccId) {
        this.ccId = ccId;
    }
    public String getCcName() {
        return ccName;
    }

    public void setCcName(String ccName) {
        this.ccName = ccName;
    }
    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }




}
