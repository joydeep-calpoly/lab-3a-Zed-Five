package lab_3a;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalTime;

import org.junit.Test;

public class Tester {
	
	@Test
	public void testConstructor() {
		boolean throwsError = false;
		
		try {
			MovieTicketPriceCalculator valid = new MovieTicketPriceCalculator(LocalTime.of(11, 00, 00), LocalTime.of(17, 00, 00), 12, 64);
		}
		catch (Exception e) {
			throwsError = true;
		}
		
		assertFalse(throwsError);
	}
	
	@Test
	public void testNullStart() {
		boolean throwsError = false;
		
		try {
			MovieTicketPriceCalculator nullStart = new MovieTicketPriceCalculator(null, LocalTime.of(17, 00, 00), 12, 64);
		}
		catch (NullPointerException e) {
			throwsError = true;
		}
		
		assertTrue(throwsError);
	}
	
	
	@Test
	public void testNullEnd() {
		boolean throwsError = false;
		
		try {
			MovieTicketPriceCalculator nullEnd = new MovieTicketPriceCalculator(LocalTime.of(11, 00, 00), null, 12, 64);
		}
		catch (NullPointerException e) {
			throwsError = true;
		}
		
		assertTrue(throwsError);
	}
	
	@Test
	public void testInvalidTime() {
		boolean throwsError = false;
		
		try {
			MovieTicketPriceCalculator invalidTime = new MovieTicketPriceCalculator(LocalTime.of(17, 00, 00), LocalTime.of(11, 00, 00), 12, 64);
		}
		catch (IllegalArgumentException e) {
			throwsError = true;
		}
		
		assertTrue(throwsError);
	}
	
	@Test
	public void testChildDiscount() {
		MovieTicketPriceCalculator mtpc = new MovieTicketPriceCalculator(LocalTime.of(11, 00, 00), LocalTime.of(17, 00, 00), 12, 64);
		
		//FOR mtpc, THE DISCOUNT IS 300 FOR EVERYONE 12 OR YOUNGER, 0 FOR OLDER (NOT INCLUDING SENIORS 64 OR OLDER)
		assertEquals(300, mtpc.computeDiscount(11));
		assertEquals(300, mtpc.computeDiscount(12));
		assertEquals(0, mtpc.computeDiscount(13));
	}
	
	@Test
	public void testSeniorDiscount() {
		MovieTicketPriceCalculator mtpc = new MovieTicketPriceCalculator(LocalTime.of(11, 00, 00), LocalTime.of(17, 00, 00), 12, 64);
		
		//FOR mtpc, THE DISCOUNT IS 400 FOR EVERYONE 64 OR OLDER, 0 FOR YOUNGER (NOT INCLUDING CHILDREN 12 OR YOUNGER)
		assertEquals(0, mtpc.computeDiscount(63));
		assertEquals(400, mtpc.computeDiscount(64));
		assertEquals(400, mtpc.computeDiscount(65));
	}
	
	@Test
	public void testNoDiscount() {
		MovieTicketPriceCalculator mtpc = new MovieTicketPriceCalculator(LocalTime.of(11, 00, 00), LocalTime.of(17, 00, 00), 12, 64);
		
		//FOR mtpc, THE DISCOUNT IS 0 FOR EVERYONE OLDER THAN 12 AND YOUNGER THAN 64
		assertEquals(0, mtpc.computeDiscount(40));
	}
	
	@Test
	public void testBeforeMatineeTime() {
		MovieTicketPriceCalculator mtpc = new MovieTicketPriceCalculator(LocalTime.of(11, 00, 00), LocalTime.of(17, 00, 00), 12, 64);
		
		//APPLIES DISCOUNTS BY AGE BRCKET (TESTED ABOVE)
		assertEquals(2700-300,mtpc.computePrice(LocalTime.of(10, 00, 00), 10));
		assertEquals(2700, mtpc.computePrice(LocalTime.of(10, 00, 00), 40));
		assertEquals(2700-400,mtpc.computePrice(LocalTime.of(10, 00, 00), 70));
	}
	
	@Test
	public void testMatineeTime() {
				MovieTicketPriceCalculator mtpc = new MovieTicketPriceCalculator(LocalTime.of(11, 00, 00), LocalTime.of(17, 00, 00), 12, 64);
		
		//APPLIES DISCOUNTS BY AGE BRCKET (TESTED ABOVE)
		assertEquals(2400-300,mtpc.computePrice(LocalTime.of(11, 00, 00), 10));
		assertEquals(2400, mtpc.computePrice(LocalTime.of(11, 00, 00), 40));
		assertEquals(2400-400,mtpc.computePrice(LocalTime.of(11, 00, 00), 70));
	}
	
	@Test
	public void testAfterMatineeTime() {
		MovieTicketPriceCalculator mtpc = new MovieTicketPriceCalculator(LocalTime.of(11, 00, 00), LocalTime.of(17, 00, 00), 12, 64);
		
		//APPLIES DISCOUNTS BY AGE BRCKET (TESTED ABOVE)
		assertEquals(2700-300,mtpc.computePrice(LocalTime.of(17, 00, 00), 10));
		assertEquals(2700, mtpc.computePrice(LocalTime.of(17, 00, 00), 40));
		assertEquals(2700-400,mtpc.computePrice(LocalTime.of(17, 00, 00), 70));
	}
	
	@Test
	public void testEdgeCaseMatinee() {
		MovieTicketPriceCalculator weird_time_mtpc = new MovieTicketPriceCalculator(LocalTime.of(11, 00, 00), LocalTime.of(11, 00, 00), 12, 64);
		
		//IF THE MATINEE STARTS AND ENDS AT THE SAME TIME, BEING EXACTLY AT THE MATINEE START TIME DOES NOT APPY THE DISCOUNT
		assertEquals(2700, weird_time_mtpc.computePrice(LocalTime.of(11, 00, 00), 40));

	}
	
	@Test
	public void testEdgeCaseAge() {
		MovieTicketPriceCalculator weird_age_mtpc = new MovieTicketPriceCalculator(LocalTime.of(11, 00, 00), LocalTime.of(17, 00, 00), 64, 12);
		MovieTicketPriceCalculator same_age_mtpc = new MovieTicketPriceCalculator(LocalTime.of(11, 00, 00), LocalTime.of(17, 00, 00), 30, 30);
		
		//IF THE MIN ELDER AGE AND MAX CHILD AGE ARE THE SAME (OR THE MIN ELDER AGE IS LOWER THAN THE MAX CHILD AGE), THE ELDER DISCOUNT TAKES PRIORITY
		assertEquals(400, weird_age_mtpc.computeDiscount(30));
		assertEquals(400, weird_age_mtpc.computeDiscount(30));

	}
}
