package com.travelmanagement.util;

import java.io.OutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.travelmanagement.dto.responseDTO.BookingResponseDTO;
import com.travelmanagement.dto.responseDTO.TravelerResponseDTO;

public class BookingInvoiceUtil {

	public static void generateInvoice(BookingResponseDTO booking, OutputStream outputStream) throws Exception {
		if (booking == null) {
			throw new IllegalArgumentException("Booking cannot be null.");
		}

		Document document = new Document(PageSize.A4, 50, 50, 50, 50);
		PdfWriter.getInstance(document, outputStream);
		document.open();

		Font titleFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, BaseColor.BLUE);
		Font subTitleFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
		Font normalFont = new Font(Font.FontFamily.HELVETICA, 11);

		Paragraph title = new Paragraph("Travel Management Invoice", titleFont);
		title.setAlignment(Element.ALIGN_CENTER);
		document.add(title);

		document.add(new Paragraph(" "));

		try {
			if (booking.getPackageImage() != null && !booking.getPackageImage().isEmpty()) {
				Image pkgImage = Image.getInstance(booking.getPackageImage());
				pkgImage.scaleToFit(500, 250);
				pkgImage.setAlignment(Element.ALIGN_CENTER);
				document.add(pkgImage);
				document.add(new Paragraph(" "));
			}
		} catch (Exception e) {
			System.out.println("Error loading package image: " + e.getMessage());
		}

		PdfPTable bookingTable = new PdfPTable(2);
		bookingTable.setWidthPercentage(100);
		bookingTable.setSpacingBefore(10f);
		bookingTable.setSpacingAfter(10f);
		bookingTable.setWidths(new float[] { 1f, 2f });

		addRowToTable(bookingTable, "Booking ID:", String.valueOf(booking.getBookingId()), subTitleFont, normalFont);
		addRowToTable(bookingTable, "Package Name:", booking.getPackageName(), subTitleFont, normalFont);

		String depDate = booking.getDepartureDateAndTime() != null
				? booking.getDepartureDateAndTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))
				: "N/A";
		addRowToTable(bookingTable, "Departure Date:", depDate, subTitleFont, normalFont);
		addRowToTable(bookingTable, "Status:", booking.getStatus(), subTitleFont, normalFont);

		double totalAmount = booking.getAmount();
		double baseAmount = totalAmount / 1.18;
		double gstAmount = totalAmount - baseAmount;

		addRowToTable(bookingTable, "Total Amount Paid (incl. 18% GST):", "₹" + String.format("%.2f", totalAmount),
				subTitleFont, normalFont);
		addRowToTable(bookingTable, "Base Amount:", "₹" + String.format("%.2f", baseAmount), subTitleFont, normalFont);
		addRowToTable(bookingTable, "GST (18%):", "₹" + String.format("%.2f", gstAmount), subTitleFont, normalFont);

		document.add(bookingTable);

		Paragraph travelersTitle = new Paragraph("Travelers List", subTitleFont);
		travelersTitle.setAlignment(Element.ALIGN_LEFT);
		document.add(travelersTitle);
		document.add(new Paragraph(" "));

		PdfPTable travelerTable = new PdfPTable(5);
		travelerTable.setWidthPercentage(100);
		travelerTable.setWidths(new float[] { 3f, 1f, 4f, 3f, 2f });

		addTravelerHeader(travelerTable, "Name");
		addTravelerHeader(travelerTable, "Age");
		addTravelerHeader(travelerTable, "Email");
		addTravelerHeader(travelerTable, "Mobile");
		addTravelerHeader(travelerTable, "Status");

		List<TravelerResponseDTO> travelers = booking.getTravelers();
		if (travelers != null) {
			boolean alternate = false;
			for (TravelerResponseDTO t : travelers) {
				BaseColor rowColor = alternate ? new BaseColor(240, 240, 240) : BaseColor.WHITE;
				addTravelerCell(travelerTable, t.getName(), normalFont, rowColor);
				addTravelerCell(travelerTable, String.valueOf(t.getAge()), normalFont, rowColor);
				addTravelerCell(travelerTable, t.getEmail(), normalFont, rowColor);
				addTravelerCell(travelerTable, t.getMobile(), normalFont, rowColor);
				addTravelerCell(travelerTable, t.getStatus(), normalFont, rowColor);
				alternate = !alternate;
			}
		}

		document.add(travelerTable);

		document.add(new Paragraph(" "));
		Paragraph thankYou = new Paragraph("Thank you for booking with us!", subTitleFont);
		thankYou.setAlignment(Element.ALIGN_CENTER);
		document.add(thankYou);

		Paragraph visitAgain = new Paragraph("Visit again for amazing travel packages!", normalFont);
		visitAgain.setAlignment(Element.ALIGN_CENTER);
		document.add(visitAgain);

		document.close();
	}

	private static void addRowToTable(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
		PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
		labelCell.setBorder(Rectangle.NO_BORDER);
		labelCell.setPadding(5f);
		table.addCell(labelCell);

		PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));
		valueCell.setBorder(Rectangle.NO_BORDER);
		valueCell.setPadding(5f);
		table.addCell(valueCell);
	}

	private static void addTravelerHeader(PdfPTable table, String text) {
		Font headerFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
		PdfPCell cell = new PdfPCell(new Phrase(text, headerFont));
		cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setPadding(5f);
		table.addCell(cell);
	}

	private static void addTravelerCell(PdfPTable table, String text, Font font, BaseColor bgColor) {
		PdfPCell cell = new PdfPCell(new Phrase(text, font));
		cell.setBackgroundColor(bgColor);
		cell.setPadding(5f);
		table.addCell(cell);
	}
}
