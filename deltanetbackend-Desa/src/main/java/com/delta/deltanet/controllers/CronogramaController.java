package com.delta.deltanet.controllers;

import com.delta.deltanet.models.dao.OutRegistroHorasRepository;
import com.delta.deltanet.models.entity.*;
import com.delta.deltanet.models.service.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.time.LocalDate;
import java.time.ZoneId;

@SuppressWarnings("all")
@CrossOrigin(origins = { "*" })
@RestController

@RequestMapping("/cronograma")
public class CronogramaController {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private ICronogramaService cronogramaService;

	@Autowired
	private IAlertaUsuarioService alertaService;

	@Autowired
	private IEmailService emailService;

	@Autowired
	private IOutFeriadoService feriadoService;

	@Autowired
	private IOutRegistroActividadService regActividadService;

	@Autowired
	private OutRegistroHorasRepository regHorasService;

	@Autowired
	public IContrato2024Service contratoService;

	@Autowired
	public IPersonaService personaService;

	@Autowired
	public IRelacionService relacionService;

	@Autowired
	public ISisParamService parametroService;

	@Autowired
	private ICronogramaModalidadService modalidadService;

	@Autowired
	private IAdmCronoHisService cronoHisService;
	@Autowired
	private CalendarioServiceImpl calendarioService;

	@Autowired
	public IPersonaService perService;

	List<Object> listado = null;
	List<Object> lstDestino = null;

	// Variables Properties del correo
	@Value("${spring.mail.host}")
	private String envHost;
	@Value("${spring.mail.port}")
	private String envPort;
	@Value("${spring.mail.username}")
	private String envUsername;
	@Value("${spring.mail.password}")
	private String envPassword;
	@Value("${spring.mail.properties.mail.smtp.auth}")
	private String envAuth;
	@Value("${spring.mail.properties.mail.smtp.starttls.enable}")
	private String envStartTls;

	public static Date addDays(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
		return cal.getTime();
	}

	// Reporte de contratos por vencer // BATCH-03
	@Scheduled(cron = "0 30 23 * * ?") // Todos los dias a las 23:59
	@GetMapping("/listaporvencer")
	public ResponseEntity<?> ListaPorVencer() {
		Map<String, Object> response = new HashMap<>();
		Date fecha = new Date();
		Date fecHoy = new Date();
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		Integer dia = calendar.get(Calendar.DAY_OF_MONTH);
		Integer mes = calendar.get(Calendar.MONTH) + 1;
		Integer yyy = calendar.get(Calendar.YEAR);
		String strFecHoy = String.format("%04d", yyy) + "-" + String.format("%02d", mes) + "-"
				+ String.format("%02d", dia);
		try {
			fecha = formato.parse(strFecHoy);
			Calendario now = calendarioService.findDate(fecha);
			OutFeriado date = feriadoService.buscaFechaFeriado(fecha);
			if (date != null || now.getIdSemanaDia().equals(6L) || now.getIdSemanaDia().equals(7L)) {
				response.put("mensaje", "No se procesa nada hoy por que es feriado");
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception x) {
			response.put("mensaje", "Ocurrio un error en el proceso");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		Calendar c = Calendar.getInstance();
		listado = cronogramaService.ListaPorVencer();
		lstDestino = alertaService.listaEmails("BATCH-03");
		StringBuilder lstColaboradores = new StringBuilder(" ");
		Iterator<Object> it = listado != null ? listado.iterator() : null;
		List<lstCrono1> lstResumenComercial = new ArrayList<>();
		List<lstCrono1> lstResumenColaborador = new ArrayList<>();
		while (it != null && it.hasNext()) {
			Object[] col = (Object[]) it.next();
			Date fVence = (Date) col[3];
			c.setTime((fVence));
			c.add(Calendar.DAY_OF_MONTH, ((Integer) col[4]) * -1);
			Date fec10 = c.getTime();
			if (fecha.after(fec10) || fecha.equals(fVence) || fecha.equals(fec10)) {
				lstCrono1 lista = new lstCrono1();
				String Empresa = cronogramaService.ObtEmpresa((Long) col[0]);
				String usuario = cronogramaService.ObtUsuario((Long) col[0]);
				String tipoDoc = cronogramaService.ObtTipoDocumento((Long) col[0]);
				String nroDoc = cronogramaService.ObtNroDocumento((Long) col[0]);

				Date fecIni = (Date) col[2];
				Date fecFin = (Date) col[3];
				long diffInMilles = fecha.getTime() - fecFin.getTime();
				long diff = TimeUnit.DAYS.convert(diffInMilles, TimeUnit.MILLISECONDS);
				lista.setCliente(Empresa);
				lista.setDscCrono("Contrato " + String.valueOf(col[5]));
				lista.setPeriodoInicial((Date) col[2]);
				lista.setPeriodoFinal((Date) col[3]);
				lista.setVcto(String.valueOf(diff));
				lista.setUsername(usuario != null ? usuario : "-");
				lista.setTipoDocumento(tipoDoc != null ? tipoDoc : "-");
				lista.setNroDocumento(nroDoc != null ? nroDoc : "-");
				lista.setDias(col[4].toString());
				lstColaboradores.append("\t - ");
				if ((Long) col[6] == 1L) {
					lstResumenColaborador.add(lista);
				} else {
					lstResumenComercial.add(lista);
				}
			}
		}
		Properties props = System.getProperties();
		props.put("mail.smtp.host", envHost);
		props.put("mail.smtp.user", envUsername);
		props.put("mail.smtp.password", envPassword);
		props.put("mail.smtp.starttls.enable", envStartTls);
		props.put("mail.smtp.port", envPort); // 587 is the port number of yahoo mail
		props.put("mail.smtp.auth", envAuth);
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(envUsername, envPassword);
			}
		});
		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(envUsername, "Deltanet"));

			Iterator<Object> tg = lstDestino != null ? lstDestino.iterator() : null;
			while (tg != null && tg.hasNext()) {
				Object[] col = (Object[]) tg.next();
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(String.valueOf(col[0])));
			}
			msg.setSubject("Deltanet - Alerta de vencimiento de contratos"); // Correo dirigido a Supervisor
			// Outsourcing
			LocalDate hoy = LocalDate.now();
			DateTimeFormatter formato2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String fechaFormateada = hoy.format(formato2);
			StringBuilder cuerpo = new StringBuilder(
					"<html><style>table {border-collapse: collapse;font-family: Tahoma, Geneva, sans-serif;}table td {padding: 15px;}\n");
			cuerpo.append(
					"table thead td {background-color: #54585d;color: #ffffff;font-weight: bold;font-size: 12px;\tborder: 1px solid #54585d;}\n");
			cuerpo.append("table tbody td {color: #636363;font-size: 10px;\tborder: 1px solid #dddfe1;}\n");
			cuerpo.append("table tbody tr {background-color: #f9fafb;text-align:center;}\n");
			cuerpo.append("table tbody tr:nth-child(odd) {background-color: #ffffff;}</style>\n");
			cuerpo.append("<body><p>Estimados, <br><br>Alertas de vencimiento de contratos al " + fechaFormateada
					+ ".</p><br>");
			// Contratos comerciales
			// Ordenar comerciales por días vencidos (de mayor a menor)
			lstResumenComercial.sort((a, b) -> Integer.parseInt(b.getVcto()) - Integer.parseInt(a.getVcto()));

			cuerpo.append("<p><strong>Contratos comerciales (" + lstResumenComercial.size() + ")</strong></p><br>");
			cuerpo.append(
					"<table style=\"border-collapse: collapse;font-family: Tahoma, Geneva, sans-serif;width: 100%;\">\n");
			cuerpo.append("<thead><tr>"
					+ "<td style=\"text-align: center; background-color: #54585d;color: #ffffff;font-weight: bold;font-size: 12px; border: 1px solid #54585d;\" width=\"6%\">Número</td>"
					+ "<td style=\"text-align: center; background-color: #54585d;color: #ffffff;font-weight: bold;font-size: 12px; border: 1px solid #54585d;\" width=\"34%\">Cliente</td>"
					+ "<td style=\"text-align: center; background-color: #54585d;color: #ffffff;font-weight: bold;font-size: 12px; border: 1px solid #54585d;\" width=\"15%\">Fecha inicio</td>"
					+ "<td style=\"text-align: center; background-color: #54585d;color: #ffffff;font-weight: bold;font-size: 12px; border: 1px solid #54585d;\" width=\"15%\">Fecha término</td>"
					+ "<td style=\"text-align: center; background-color: #54585d;color: #ffffff;font-weight: bold;font-size: 12px; border: 1px solid #54585d;\" width=\"15%\">Días de alerta</td>"
					+ "<td style=\"text-align: center; background-color: #54585d;color: #ffffff;font-weight: bold;font-size: 12px; border: 1px solid #54585d;\" width=\"15%\">Días vencidos</td>"
					+ "</tr></thead><tbody>\n");
			for (int i = 0; i < lstResumenComercial.size(); i++) {
				cuerpo.append("<tr style=\"background-color: #f9fafb;text-align:center;\">"
						+ "<td style=\"padding-left: 5px;text-align: center;color: #636363;font-size: 9px; border: 1px solid #dddfe1;\" width=\"5%\">"
						+ (i + 1)
						+ "</td><td style=\"padding-left: 10px;text-align: left;color: #636363;font-size: 10px; border: 1px solid #dddfe1;\" width=\"34%\">"
						+ lstResumenComercial.get(i).getCliente()
						+ "</td><td style=\"color: #636363;font-size: 9px; border: 1px solid #dddfe1;\" width=\"15%\">"
						+ lstResumenComercial.get(i).getPeriodoInicial().toString()
						+ "</td><td style=\"color: #636363;font-size: 9px; border: 1px solid #dddfe1;\" width=\"15%\">"
						+ lstResumenComercial.get(i).getPeriodoFinal().toString()
						+ "</td><td style=\"color: #636363;font-size: 9px; border: 1px solid #dddfe1;\" width=\"15%\">"
						+ lstResumenComercial.get(i).getDias()
						+ "</td><td style=\"color: #636363;font-size: 10px; border: 1px solid #dddfe1;\" width=\"15%\">"
						+ lstResumenComercial.get(i).getVcto() + "</td></tr>");
			}
			cuerpo.append("</tbody></table><br>");
			// Contratos colaboradores
			cuerpo.append("<p><strong>Contratos colaboradores (" + lstResumenColaborador.size() + ")</strong></p><br>");
			cuerpo.append(
					"<table style=\"border-collapse: collapse;font-family: Tahoma, Geneva, sans-serif;width: 100%;\">\n");
			cuerpo.append("<thead><tr>"
					+ "<td style=\"text-align: center; background-color: #54585d;color: #ffffff;font-weight: bold;font-size: 12px; border: 1px solid #54585d;\" width=\"6%\">Número</td>"
					+ "<td style=\"text-align: center; background-color: #54585d;color: #ffffff;font-weight: bold;font-size: 12px; border: 1px solid #54585d;\" width=\"20%\">Colaborador</td>"
					+ "<td style=\"text-align: center; background-color: #54585d;color: #ffffff;font-weight: bold;font-size: 12px; border: 1px solid #54585d;\" width=\"10%\">Usuario</td>"
					+ "<td style=\"text-align: center; background-color: #54585d;color: #ffffff;font-weight: bold;font-size: 12px; border: 1px solid #54585d;\" width=\"10%\">Tipo de documento</td>"
					+ "<td style=\"text-align: center; background-color: #54585d;color: #ffffff;font-weight: bold;font-size: 12px; border: 1px solid #54585d;\" width=\"10%\">Número de documento</td>"
					+ "<td style=\"text-align: center; background-color: #54585d;color: #ffffff;font-weight: bold;font-size: 12px; border: 1px solid #54585d;\" width=\"12%\">Fecha inicio</td>"
					+ "<td style=\"text-align: center; background-color: #54585d;color: #ffffff;font-weight: bold;font-size: 12px; border: 1px solid #54585d;\" width=\"12%\">Fecha término</td>"
					+ "<td style=\"text-align: center; background-color: #54585d;color: #ffffff;font-weight: bold;font-size: 12px; border: 1px solid #54585d;\" width=\"10%\">Días de alerta</td>"
					+ "<td style=\"text-align: center; background-color: #54585d;color: #ffffff;font-weight: bold;font-size: 12px; border: 1px solid #54585d;\" width=\"10%\">Días vencidos</td>"
					+ "</tr></thead><tbody>\n");
			// Ordenar colaboradores por días vencidos (de mayor a menor)
			lstResumenColaborador.sort((a, b) -> Integer.parseInt(b.getVcto()) - Integer.parseInt(a.getVcto()));
			for (int i = 0; i < lstResumenColaborador.size(); i++) {
				lstCrono1 colab = lstResumenColaborador.get(i);
				cuerpo.append("<tr style=\"background-color: #f9fafb;text-align:center;\">"
						+ "<td style=\"padding-left: 5px;text-align: center;color: #636363;font-size: 9px; border: 1px solid #dddfe1;\" width=\"6%\">"
						+ (i + 1)
						+ "</td><td style=\"padding-left: 10px;text-align: left;color: #636363;font-size: 10px; border: 1px solid #dddfe1;\" width=\"20%\">"
						+ colab.getCliente()
						+ "</td><td style=\"padding-left: 5px;text-align: left;color: #636363;font-size: 9px; border: 1px solid #dddfe1;\" width=\"10%\">"
						+ (colab.getUsername() != null ? colab.getUsername() : "-")
						+ "</td><td style=\"padding-left: 5px;text-align: center;color: #636363;font-size: 10px; border: 1px solid #dddfe1;\" width=\"10%\">"
						+ (colab.getTipoDocumento() != null ? colab.getTipoDocumento() : "-")
						+ "</td><td style=\"padding-left: 5px;text-align: center;color: #636363;font-size: 10px; border: 1px solid #dddfe1;\" width=\"10%\">"
						+ (colab.getNroDocumento() != null ? colab.getNroDocumento() : "-")
						+ "</td><td style=\"color: #636363;font-size: 9px; border: 1px solid #dddfe1;\" width=\"12%\">"
						+ colab.getPeriodoInicial().toString()
						+ "</td><td style=\"color: #636363;font-size: 9px; border: 1px solid #dddfe1;\" width=\"12%\">"
						+ colab.getPeriodoFinal().toString()
						+ "</td><td style=\"color: #636363;font-size: 9px; border: 1px solid #dddfe1;\" width=\"10%\">"
						+ colab.getDias()
						+ "</td><td style=\"color: #636363;font-size: 10px; border: 1px solid #dddfe1;\" width=\"10%\">"
						+ colab.getVcto() + "</td></tr>");
			}
			cuerpo.append("</tbody></table><br><br>");
			cuerpo.append("<p>Saludos cordiales, <br>Sistema Deltanet</p></body></html>\n");
			msg.setContent(cuerpo.toString(), "text/html; charset=utf-8");
			Transport transport = session.getTransport();
			transport.connect();
			transport.send(msg);
			transport.close();
			// Transport.send(msg);
		} catch (Exception e) {
			response.put("mensaje", "Error el enviar correo");
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "Correo enviado satisfactoriamente");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/busqueda")
	public ResponseEntity<?> Busqueda(
			@RequestParam(required = false, name = "fecini") @DateTimeFormat(pattern = "dd/MM/yyyy") Date fecIni,
			@RequestParam(required = false, name = "fecfin") @DateTimeFormat(pattern = "dd/MM/yyyy") Date fecFin,
			@RequestParam(required = false, name = "tipodoc") Long tipoDoc,
			@RequestParam(required = false, name = "nrodoc") String nroDoc,
			@RequestParam(required = false, name = "nombres") String nombres,
			@RequestParam(required = false, name = "estado") Long estado) throws ParseException {
		Map<String, Object> response = new HashMap<>();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		// SimpleDateFormat formatter1 = new SimpleDateFormat("MMM d, yyyy",
		// Locale.ENGLISH);
		// String fechaActual = DateFormat.getDateInstance().format(new Date());

		Date fecha = new Date();
		Calendar c = Calendar.getInstance();

		if (fecIni == null && fecFin == null && tipoDoc == null && nroDoc == null && nombres == null
				&& estado == null) {
			listado = cronogramaService.BusquedaFiltrada();
		}
		if (fecIni != null && fecFin != null && tipoDoc == null && nroDoc == null && nombres == null
				&& estado == null) {
			listado = cronogramaService.BusquedaFiltrada(fecIni, fecFin);
		}
		if (fecIni != null && fecFin != null && tipoDoc != null && nroDoc == null && nombres == null
				&& estado == null) {
			listado = cronogramaService.BusquedaFiltrada(fecIni, fecFin, tipoDoc);
		}
		if (fecIni != null && fecFin != null && tipoDoc != null && nroDoc != null && nombres == null
				&& estado == null) {
			listado = cronogramaService.BusquedaFiltrada(fecIni, fecFin, tipoDoc, nroDoc);
		}
		if (fecIni != null && fecFin != null && tipoDoc != null && nroDoc != null && nombres != null
				&& estado == null) {
			String[] palabras = nombres.split("\\s+");
			StringBuilder fullname = new StringBuilder("%");
			for (String s : palabras) {
				fullname.append(s).append('%');
			}

			listado = cronogramaService.BusquedaFiltrada(fecIni, fecFin, tipoDoc, nroDoc, fullname.toString());
		}

		if (fecIni != null && fecFin != null && tipoDoc != null && nroDoc != null && nombres != null
				&& estado != null) {
			String[] palabras = nombres.split("\\s+");
			StringBuilder fullname = new StringBuilder("%");
			for (String s : palabras) {
				fullname.append(s).append('%');
			}

			listado = cronogramaService.BusquedaFiltrada(fecIni, fecFin, tipoDoc, nroDoc, fullname.toString(), estado);
		}

		if (fecIni == null && fecFin == null && tipoDoc != null && nroDoc == null && nombres == null
				&& estado == null) {
			listado = cronogramaService.BusquedaFiltrada(tipoDoc);
		}

		if (fecIni == null && fecFin == null && tipoDoc != null && nroDoc != null && nombres == null
				&& estado == null) {
			listado = cronogramaService.BusquedaFiltrada(tipoDoc, nroDoc);
		}

		if (fecIni == null && fecFin == null && tipoDoc != null && nroDoc != null && nombres != null
				&& estado == null) {
			String[] palabras = nombres.split("\\s+");
			StringBuilder fullname = new StringBuilder("%");
			for (String s : palabras) {
				fullname.append(s).append('%');
			}
			listado = cronogramaService.BusquedaFiltrada(tipoDoc, nroDoc, fullname.toString());
		}
		if (fecIni == null && fecFin == null && tipoDoc != null && nroDoc != null && nombres != null
				&& estado != null) {
			String[] palabras = nombres.split("\\s+");
			StringBuilder fullname = new StringBuilder("%");
			for (String s : palabras) {
				fullname.append(s).append('%');
			}
			listado = cronogramaService.BusquedaFiltrada(tipoDoc, nroDoc, fullname.toString(), estado);
		}
		if (fecIni == null && fecFin == null && tipoDoc == null && nroDoc != null && nombres == null
				&& estado == null) {
			listado = cronogramaService.BusquedaFiltrada(nroDoc);
		}
		if (fecIni == null && fecFin == null && tipoDoc == null && nroDoc != null && nombres != null
				&& estado == null) {
			String[] palabras = nombres.split("\\s+");
			StringBuilder fullname = new StringBuilder("%");
			for (String s : palabras) {
				fullname.append(s).append('%');
			}
			listado = cronogramaService.BusquedaFiltrada(nroDoc, fullname.toString());
		}
		if (fecIni == null && fecFin == null && tipoDoc == null && nroDoc != null && nombres != null
				&& estado != null) {
			String[] palabras = nombres.split("\\s+");
			StringBuilder fullname = new StringBuilder("%");
			for (String s : palabras) {
				fullname.append(s).append('%');
			}
			listado = cronogramaService.BusquedaFiltrada(nroDoc, fullname.toString(), estado);
		}
		if (fecIni == null && fecFin == null && tipoDoc == null && nroDoc == null && nombres != null
				&& estado == null) {
			String[] palabras = nombres.split("\\s+");
			StringBuilder fullname = new StringBuilder("%");
			for (String s : palabras) {
				fullname.append(s).append('%');
			}
			listado = cronogramaService.BusquedaFiltrada1(fullname.toString());
		}
		if (fecIni == null && fecFin == null && tipoDoc == null && nroDoc == null && nombres != null
				&& estado != null) {
			String[] palabras = nombres.split("\\s+");
			StringBuilder fullname = new StringBuilder("%");
			for (String s : palabras) {
				fullname.append(s).append('%');
			}
			listado = cronogramaService.BusquedaFiltrada(fullname.toString(), estado);
		}
		if (fecIni == null && fecFin == null && tipoDoc == null && nroDoc == null && nombres == null
				&& estado != null) {
			listado = cronogramaService.BusquedaFiltrada1(estado);
		}
		if (fecIni != null && fecFin != null && tipoDoc == null && nroDoc != null && nombres == null
				&& estado == null) {
			listado = cronogramaService.BusquedaFiltrada(fecIni, fecFin, nroDoc);
		}
		if (fecIni != null && fecFin != null && tipoDoc == null && nroDoc == null && nombres != null
				&& estado == null) {
			String[] palabras = nombres.split("\\s+");
			StringBuilder fullname = new StringBuilder("%");
			for (String s : palabras) {
				fullname.append(s).append('%');
			}
			listado = cronogramaService.BusquedaFiltrada1(fecIni, fecFin, fullname.toString());
		}
		if (fecIni != null && fecFin != null && tipoDoc == null && nroDoc == null && nombres == null
				&& estado != null) {
			listado = cronogramaService.BusquedaFiltrada1(fecIni, fecFin, estado);
		}
		if (fecIni == null && fecFin == null && tipoDoc != null && nroDoc == null && nombres != null
				&& estado == null) {
			String[] palabras = nombres.split("\\s+");
			StringBuilder fullname = new StringBuilder("%");
			for (String s : palabras) {
				fullname.append(s).append('%');
			}
			listado = cronogramaService.BusquedaFiltrada1(tipoDoc, fullname.toString());
		}
		if (fecIni == null && fecFin == null && tipoDoc != null && nroDoc == null && nombres == null
				&& estado != null) {
			listado = cronogramaService.BusquedaFiltrada(tipoDoc, estado);
		}
		if (fecIni == null && fecFin == null && tipoDoc == null && nroDoc != null && nombres == null
				&& estado != null) {
			listado = cronogramaService.BusquedaFiltrada1(nroDoc, estado);
		}
		if (fecIni != null && fecFin != null && tipoDoc != null && nroDoc == null && nombres != null
				&& estado == null) {
			String[] palabras = nombres.split("\\s+");
			StringBuilder fullname = new StringBuilder("%");
			for (String s : palabras) {
				fullname.append(s).append('%');
			}
			listado = cronogramaService.BusquedaFiltrada1(fecIni, fecFin, tipoDoc, fullname.toString());
		}
		if (fecIni != null && fecFin != null && tipoDoc != null && nroDoc == null && nombres == null
				&& estado != null) {
			listado = cronogramaService.BusquedaFiltrada(fecIni, fecFin, tipoDoc, estado);
		}
		if (fecIni != null && fecFin != null && tipoDoc == null && nroDoc != null && nombres != null
				&& estado == null) {
			String[] palabras = nombres.split("\\s+");
			StringBuilder fullname = new StringBuilder("%");
			for (String s : palabras) {
				fullname.append(s).append('%');
			}
			listado = cronogramaService.BusquedaFiltrada(fecIni, fecFin, nroDoc, fullname.toString());
		}
		if (fecIni != null && fecFin != null && tipoDoc == null && nroDoc != null && nombres == null
				&& estado != null) {
			listado = cronogramaService.BusquedaFiltrada(fecIni, fecFin, nroDoc, estado);
		}
		if (fecIni != null && fecFin != null && tipoDoc == null && nroDoc == null && nombres != null
				&& estado != null) {
			String[] palabras = nombres.split("\\s+");
			StringBuilder fullname = new StringBuilder("%");
			for (String s : palabras) {
				fullname.append(s).append('%');
			}
			listado = cronogramaService.BusquedaFiltrada1(fecIni, fecFin, fullname.toString(), estado);
		}
		if (fecIni == null && fecFin == null && tipoDoc != null && nroDoc != null && nombres == null
				&& estado != null) {
			listado = cronogramaService.BusquedaFiltrada(tipoDoc, nroDoc, estado);
		}
		if (fecIni == null && fecFin == null && tipoDoc != null && nroDoc == null && nombres != null
				&& estado != null) {
			String[] palabras = nombres.split("\\s+");
			StringBuilder fullname = new StringBuilder("%");
			for (String s : palabras) {
				fullname.append(s).append('%');
			}
			listado = cronogramaService.BusquedaFiltrada1(tipoDoc, fullname.toString(), estado);
		}
		if (fecIni != null && fecFin != null && tipoDoc == null && nroDoc != null && nombres != null
				&& estado != null) {
			String[] palabras = nombres.split("\\s+");
			StringBuilder fullname = new StringBuilder("%");
			for (String s : palabras) {
				fullname.append(s).append('%');
			}
			listado = cronogramaService.BusquedaFiltrada(fecIni, fecFin, nroDoc, fullname.toString(), estado);
		}
		if (fecIni != null && fecFin != null && tipoDoc != null && nroDoc == null && nombres != null
				&& estado != null) {
			String[] palabras = nombres.split("\\s+");
			StringBuilder fullname = new StringBuilder("%");
			for (String s : palabras) {
				fullname.append(s).append('%');
			}
			listado = cronogramaService.BusquedaFiltrada(fecIni, fecFin, tipoDoc, fullname.toString(), estado);
		}
		if (fecIni != null && fecFin != null && tipoDoc != null && nroDoc != null && nombres == null
				&& estado != null) {
			listado = cronogramaService.BusquedaFiltrada1(fecIni, fecFin, tipoDoc, nroDoc, estado);
		}

		if (listado.isEmpty()) {
			response.put("mensaje", "No se obtuvo resultados en la consulta");
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		Iterator<Object> it = listado != null ? listado.iterator() : null;

		List<lstCrono2> lstResumen = new ArrayList<>();

		boolean porVencer = false;

		while (it != null && it.hasNext()) {
			Object[] col = (Object[]) it.next();

			Date fVence = (Date) col[4];
			c.setTime((fVence));
			c.add(Calendar.DAY_OF_MONTH, -10);
			// String nuevaFecha = formatter.format(c.getTime());
			// Date fec10 = formatter.parse(nuevaFecha);
			Date fec10 = c.getTime();

			if ((fecha.after(fec10) && fecha.before(fVence)) || fecha.equals(fVence) || fecha.equals(fec10)) {
				porVencer = true;
			}

			lstCrono2 lista = new lstCrono2();
			lista.setIdCrono((Long) col[0]);
			lista.setIdContrato((Long) col[1]);
			lista.setDesCrono(String.valueOf(col[2]));
			if (col[3] != null)
				lista.setPeriodoInicial(formatter.format((Date) col[3]));
			if (col[4] != null)
				lista.setPeriodoFinal(formatter.format((Date) col[4]));
			if (col[5] != null)
				lista.setNroOC(String.valueOf(col[5]));
			if (col[6] != null)
				lista.setFechaOC(formatter.format((Date) col[6]));
			lista.setEstCrono((Long) col[7]);
			lista.setIdPerCro((Long) col[8]);
			lista.setIdentifica(String.valueOf(col[9]));
			lista.setTipoDocDesc(String.valueOf(col[10]));
			lista.setNroDocPer(String.valueOf(col[11]));
			lista.setPorVencer(porVencer);

			lstResumen.add(lista);
		}

		response.put("mensaje", "Lista generada satisfactoriamente");
		return new ResponseEntity<>(lstResumen, HttpStatus.OK);
	}

	@GetMapping("/index")
	public ResponseEntity<?> Index(@RequestParam(name = "idCro") Long id) {
		Map<String, Object> response = new HashMap<>();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

		Object registro = cronogramaService.BuscaId(id);
		if (registro == null) {
			response.put("mensaje", "Id no encontrado");
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		Object[] col = (Object[]) registro;
		lstCrono3 lista = new lstCrono3();
		lista.setId((Long) col[0]);
		lista.setIdContrato((Long) col[1]);
		lista.setDesCrono(String.valueOf(col[2]));
		lista.setNombres(String.valueOf(col[3]));
		lista.setPeriodoInicial(formatter.format((Date) col[4]));
		lista.setPeriodoFinal(formatter.format((Date) col[5]));
		lista.setNroOC(String.valueOf(col[6]));
		lista.setFechaOC(formatter.format((Date) col[7]));
		lista.setEstCrono((Long) col[8]);

		return new ResponseEntity<>(lista, HttpStatus.OK);
	}

	@PostMapping("/update")
	public ResponseEntity<?> update(@RequestParam(name = "idCro") Long id,
			@RequestParam(required = false, name = "fecVence") @DateTimeFormat(pattern = "dd/MM/yyyy") Date fecVence,
			@RequestParam(required = false, name = "estado") Long estado) {
		Map<String, Object> response = new HashMap<>();
		Long actEstado;
		Long newEstado = 0L;
		if (fecVence == null && estado == null) {
			response.put("mensaje", "Debe enviar por lo menos un campo a actualizar");
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		Cronograma registro = new Cronograma();
		try {
			registro = cronogramaService.findById(id);
		} catch (Exception e) {
			response.put("mensaje", "No se encuentra el ID en la base de datos");
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}

		actEstado = registro.getEstado();
		if (actEstado == 3) {
			if (estado == 1 || estado == 2) {
				newEstado = estado;
			} else {
				response.put("mensaje", "El cronograma tiene estado 3., se espera un nuevo estado 1 ó 2");
				return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		if (actEstado == 1) {
			if (estado == 1 || estado == 2 || estado == 3 || estado == 4) {
				newEstado = estado;
			} else {
				response.put("mensaje", "El cronograma tiene estado 1., se espera un nuevo estado 2, 3 ó 4");
				return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		if (actEstado == 2) {
			if (estado == 1 || estado == 2 || estado == 4) {
				newEstado = estado;
			} else {
				response.put("mensaje", "El cronograma tiene estado 2., se espera un nuevo estado 1 ó 4");
				return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		if (actEstado == 4) {
			response.put("mensaje", "El cronograma tiene estado 4., no se puede cambiar el estado.");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (fecVence != null)
			registro.setPeriodoFinal(fecVence);
		// if (estado != null) registro.setEstado(estado);
		if (estado != null)
			registro.setEstado(newEstado);
		try {
			cronogramaService.save(registro);
		} catch (Exception e) {
			response.put("mensaje", "Error al actualizar la tabla de cronograma");
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El registro se actualizo satisfactoriamente");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	private boolean buscaFecha(List<Object> fechas, Date fecha) {
		boolean encontro = false;
		if (fechas.isEmpty()) {
			encontro = false;
		} else {
			Iterator<Object> it = fechas != null ? fechas.iterator() : null;
			while (it != null && it.hasNext()) {
				Object[] col = (Object[]) it.next();
				if (fecha.equals((Date) col[0])) {
					encontro = true;
				}
			}
		}
		return encontro;
	}

	public Map<String, Object> logColaborador(Date fecha) throws ParseException {
		Map<String, Object> response = new HashMap<>();
		List<Object> listado1 = null;
		List<Object> correo1 = null;
		List<Object> feriados = null;
		List<Object> compensaciones = null;
		List<Object> lstRegHoras = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		SimpleDateFormat formaCorreo = new SimpleDateFormat("dd.MMM.yyyy", Locale.ENGLISH);
		int idPer;
		int idCron;
		boolean sinCronograma;
		OutRegistroActividad regActi = new OutRegistroActividad();
		String email = null;
		StringBuilder pendientes = new StringBuilder();

		/*
		 * Se obtiene la fecha del viernes en Date y String
		 */
		Calendar calendar = Calendar.getInstance();
		calendar.setTime((fecha));
		String strFecha = formatter.format(calendar.getTime());
		Date fecViernes = formatter.parse(strFecha);

		/*
		 * Se obtiene la fecha del Lunes en Date y String, restando en 4 dias a la fecha
		 * del viernes
		 */
		calendar.add(Calendar.DATE, -4);
		String strLunes = formatter.format(calendar.getTime());
		Date fecLunes = formatter.parse(strLunes);

		feriados = feriadoService.buscaRangoFecha(fecLunes, fecViernes);

		Timestamp fecFREG;
		String nomCompleto;
		Date fReg;

		/*
		 * Se obtienes a las personas de adm_relacion con tipo de relacion = 4 y estado
		 * = 1
		 */
		listado = cronogramaService.listaRelacionOutsourcing();
		Iterator<Object> it = listado != null ? listado.iterator() : null;

		while (it != null && it.hasNext()) {
			Object[] col = (Object[]) it.next();
			idPer = ((Long) col[1]).intValue();
			nomCompleto = String.valueOf(col[2]) + " " + String.valueOf(col[3]) + " " + String.valueOf(col[4]) + " ("
					+ String.valueOf(col[5]) + ": " + String.valueOf(col[6]) + ")";
			pendientes = new StringBuilder();
			compensaciones = regActividadService.BuscarPorRangoFechas((Long) col[1], fecLunes, fecViernes, 8L);

			// Obtiene los correos activos de la persona

			correo1 = emailService.findByCorreosPer(1, (Long) col[1]);
			if (!correo1.isEmpty()) {
				Iterator<Object> iem = correo1 != null ? correo1.iterator() : null;
				while (iem != null && iem.hasNext()) {
					Object[] colMail = (Object[]) iem.next();
					if (email == null) {
						email = String.valueOf(colMail[3]);
					}
				}
			}
			// Lista las horas registradas de la persona en referencia al cronograma activo

			lstRegHoras = regHorasService.findByPersonaCronogramaFechaEstado(idPer, fecLunes, fecha, 1);

			Iterator<Object> ireg = lstRegHoras != null ? lstRegHoras.iterator() : null;
			fReg = fecLunes;
			calendar.setTime(fReg);
			fecFREG = new Timestamp(fReg.getTime());
			Date fdata = null;
			Long nroHoras;
			boolean ciclo = true;
			while (ireg != null && ireg.hasNext()) {
				Object[] colReg = (Object[]) ireg.next();
				calendar.setTime((Date) colReg[0]);
				fdata = calendar.getTime();
				nroHoras = (Long) colReg[1];
				ciclo = true;
				while (ciclo) {
					boolean compensacion = buscaFecha(compensaciones, fReg);
					if (compensacion) {
						ciclo = true;
					} else {
						boolean asueto = buscaFecha(feriados, fReg);
						if (asueto) {
							ciclo = true;
						} else {
							if (fReg.equals(fdata)) {
								if (nroHoras < 8) {
									pendientes.append(formaCorreo.format(fReg.getTime())).append(" faltan ")
											.append(8 - nroHoras).append(" horas por registrar-");
								}
								ciclo = false;
							} else {
								pendientes.append(formaCorreo.format(fReg.getTime()))
										.append(" no cuenta con registro de horas-");
							}
						}
					}
					calendar.setTime((fReg));
					calendar.add(Calendar.DAY_OF_MONTH, 1);
					fReg = calendar.getTime();
					fecFREG = new Timestamp(fReg.getTime());
					if (fReg == fecViernes) {
						ciclo = false;
					}
				}
			}

			if (fdata == null) {
				calendar.setTime((fecLunes));
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				fdata = calendar.getTime();
			}

			while (fdata.before(fecViernes)) {
				calendar.setTime((fdata));
				calendar.add(Calendar.DAY_OF_MONTH, 1);
				fdata = calendar.getTime();
				boolean asueto2 = buscaFecha(feriados, fdata);
				if (asueto2 == false) {
					pendientes.append(formaCorreo.format(fdata.getTime())).append(" no cuenta con registro de horas-");
				} else {
					pendientes.append(formaCorreo.format(fdata.getTime())).append(" el dia es feriado-");
				}
			}
			if (email != null && !pendientes.toString().isEmpty()) {
				Properties props = System.getProperties();
				props.put("mail.smtp.host", envHost);
				props.put("mail.smtp.user", envUsername);
				props.put("mail.smtp.password", envPassword);
				props.put("mail.smtp.starttls.enable", envStartTls);
				props.put("mail.smtp.port", envPort); // 587 is the port number of yahoo mail
				props.put("mail.smtp.auth", envAuth);
				Session session = Session.getInstance(props, new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(envUsername, envPassword);
					}
				});

				try {
					Thread.sleep(1500); // 1.5 secs
					Message msg = new MimeMessage(session);
					msg.setFrom(new InternetAddress(envUsername, "Deltanet"));
					msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
					/*
					 * if (sinCronograma) { msg.setSubject("Deltanet - Reporte de Horas Semanal");
					 * msg.setText("Estimado(a) colaborador(a): " + nomCompleto +
					 * "...\n\nPor la presente se le informa que actualmente no cuenta con un cronograma de actividades activo."
					 * +
					 * "...\n\nPor favor sirvase solicitar al Área de RECURSOS HUMANOS, a través de un ticket Deltanet, para la regularización correspondiente."
					 * ); } else {
					 */
					msg.setSubject("Deltanet - Colaborador - Reporte de horas individual"); // Correo dirigido a cada
					// colaborador Outsourcing
					// msg.setSubject("Alerta de Horas pendientes de registro");
					String[] lineas = pendientes.toString().split("-");
					StringBuilder cuerpo = new StringBuilder("Estimado(a) colaborador(a): ");
					cuerpo.append(nomCompleto);
					cuerpo.append("\n\n");
					cuerpo.append(
							"Por la presente se le informa que en las siguientes fechas, tiene horas pendiente de registro.\n\n");
					for (String linea : lineas) {
						cuerpo.append(linea);
						cuerpo.append("\n\n");
					}
					msg.setText(cuerpo.toString());
					// }

					Transport.send(msg);
				} catch (Exception e) {
					response.put("msgEmail", "Error el enviar correo a la cuenta: " + email);
					response.put("errEmail", e.getMessage());
				}
			}
			email = null;
		}

		response.put("mensaje", "Se concluyo la ejecucion del servicio");
		return (response);
	}

	public Map<String, Object> logReproceso(Date fecha, String title) throws ParseException {
		Map<String, Object> response = new HashMap<>();
		List<Object> listado1 = null;
		List<Object> correo1 = null;
		List<Object> feriados = null;
		List<Object> compensaciones = null;
		List<Object> lstRegHoras = null;
		List<lstCroTabla> lstEnvioMail = new ArrayList<>();
		Object regEmpresa;
		Date fechaCampo;
		String strFecFld;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		SimpleDateFormat fmtCampos = new SimpleDateFormat("dd/MM", Locale.ENGLISH);
		SimpleDateFormat formaCorreo = new SimpleDateFormat("dd.MMM.yyyy", Locale.ENGLISH);
		int idPer;
		int idCron;
		Long idCtto;
		int numDia;
		String vlrHra;
		String msgEnvio;
		boolean sinCronograma;
		OutRegistroActividad regActi = new OutRegistroActividad();
		String email = null;
		StringBuilder pendientes = new StringBuilder();
		lstCroTabla regEnvio;

		/*
		 * Se obtiene la fecha del viernes en Date y String
		 */
		Calendar calendar = Calendar.getInstance();
		calendar.setTime((fecha));
		String strFecha = formatter.format(calendar.getTime());
		Date fecViernes = formatter.parse(strFecha);

		/*
		 * Se obtiene la fecha del Lunes en Date y String, restando en 4 dias a la fecha
		 * del viernes
		 */
		calendar.add(Calendar.DATE, -4);
		String strLunes = formatter.format(calendar.getTime());
		Date fecLunes = formatter.parse(strLunes);

		feriados = feriadoService.buscaRangoFecha(fecLunes, fecViernes);

		Timestamp fecFREG;
		String nomCompleto;
		Date fReg;

		/*
		 * Se obtienes a las personas de adm_relacion con tipo de relacion = 4 y estado
		 * = 1
		 */
		listado = cronogramaService.listaRelacionOutsourcing();
		Iterator<Object> it = listado != null ? listado.iterator() : null;

		while (it != null && it.hasNext()) {
			Object[] col = (Object[]) it.next();
			idPer = ((Long) col[1]).intValue();
			nomCompleto = String.valueOf(col[2]) + " " + String.valueOf(col[3]) + " " + String.valueOf(col[4]) + " ("
					+ String.valueOf(col[5]) + ": " + String.valueOf(col[6]) + ")";
			pendientes = new StringBuilder();
			compensaciones = regActividadService.BuscarPorRangoFechas((Long) col[1], fecLunes, fecViernes, 8L);
			msgEnvio = "";

			regEnvio = new lstCroTabla();
			regEnvio.setIdColaborador((Long) col[1]);
			regEnvio.setNomCompleto(
					String.valueOf(col[2]) + " " + String.valueOf(col[3]) + " " + String.valueOf(col[4]));
			regEnvio.setHraL("-");
			regEnvio.setHraM("-");
			regEnvio.setHraX("-");
			regEnvio.setHraJ("-");
			regEnvio.setHraV("-");

			/*
			 * Lista los cronogramas de la persona con estado = 1
			 */
			sinCronograma = true;
			listado1 = cronogramaService.listaCronoPorIdPer((Long) col[1]);
			if (!listado1.isEmpty()) {
				sinCronograma = false;
				Iterator<Object> icro = listado1 != null ? listado1.iterator() : null;
				while (icro != null && icro.hasNext()) {
					Object[] colCro = (Object[]) icro.next();
					idCron = ((Long) colCro[0]).intValue();
					idCtto = (Long) colCro[1]; // idContrato
					regEmpresa = contratoService.buscaCliente(idCtto);
					/*
					 * Lista las horas registradas de la persona en referencia al cronograma activo
					 */
					lstRegHoras = regHorasService.findByPersonaCronogramaFechaEstado(idPer, fecLunes, fecha, 1);

					Iterator<Object> ireg = lstRegHoras != null ? lstRegHoras.iterator() : null;
					fReg = fecLunes;
					calendar.setTime(fReg);
					fecFREG = new Timestamp(fReg.getTime());
					Date fdata = null;
					Long nroHoras;
					boolean ciclo = true;
					numDia = 0;
					while (ireg != null && ireg.hasNext()) {
						numDia = numDia + 1;
						Object[] colReg = (Object[]) ireg.next();
						calendar.setTime((Date) colReg[0]);
						fdata = calendar.getTime();
						nroHoras = (Long) colReg[1];
						ciclo = true;
						while (ciclo) {
							boolean compensacion = buscaFecha(compensaciones, fReg);
							if (compensacion) {
								vlrHra = "C";
								ciclo = true;
							} else {
								boolean asueto = buscaFecha(feriados, fReg);
								if (asueto) {
									vlrHra = "F";
									ciclo = true;
								} else {
									if (fReg.equals(fdata)) {
										if (nroHoras < 8) {
											vlrHra = nroHoras.toString();
											msgEnvio = "Tiene horas por registrar";
											pendientes.append(formaCorreo.format(fReg.getTime())).append(" faltan ")
													.append(8 - nroHoras).append(" horas por registrar-");
										} else {
											vlrHra = "8";
										}
										ciclo = false;
									} else {
										vlrHra = "0";
										msgEnvio = "Tiene horas por registrar";
										pendientes.append(formaCorreo.format(fReg.getTime()))
												.append(" no cuenta con registro de horas-");
									}
								}
							}

							if (numDia == 1) {
								regEnvio.setFecL(fReg);
								regEnvio.setHraL(vlrHra);
							}
							if (numDia == 2) {
								regEnvio.setFecM(fReg);
								regEnvio.setHraM(vlrHra);
							}
							if (numDia == 3) {
								regEnvio.setFecX(fReg);
								regEnvio.setHraX(vlrHra);
							}
							if (numDia == 4) {
								regEnvio.setFecJ(fReg);
								regEnvio.setHraJ(vlrHra);
							}
							if (numDia == 5) {
								regEnvio.setFecV(fReg);
								regEnvio.setHraV(vlrHra);
							}

							calendar.setTime((fReg));
							calendar.add(Calendar.DAY_OF_MONTH, 1);
							fReg = calendar.getTime();
							fecFREG = new Timestamp(fReg.getTime());
							if (fReg == fecViernes) {
								ciclo = false;
							}
						}
					}

					if (fdata == null) {
						calendar.setTime((fecLunes));
						calendar.add(Calendar.DAY_OF_MONTH, -1);
						fdata = calendar.getTime();
					}

					while (fdata.before(fecViernes)) {
						calendar.setTime((fdata));
						calendar.add(Calendar.DAY_OF_MONTH, 1);
						fdata = calendar.getTime();
						boolean asueto = buscaFecha(feriados, fdata);
						if (asueto) {
							int diaSem = calendar.get(Calendar.DAY_OF_WEEK);
							if (diaSem == 2)
								regEnvio.setHraL("F");
							if (diaSem == 3)
								regEnvio.setHraM("F");
							if (diaSem == 4)
								regEnvio.setHraX("F");
							if (diaSem == 5)
								regEnvio.setHraJ("F");
							if (diaSem == 6)
								regEnvio.setHraV("F");
						}

						msgEnvio = "Tiene horas por registrar";
						pendientes.append(formaCorreo.format(fdata.getTime()))
								.append(" no cuenta con registro de horas-");
					}
				}
			} else {
				msgEnvio = "No tiene cronograma activo";
				pendientes.append("No tiene cronograma activo.");
			}
			regEnvio.setObservacion(msgEnvio);
			lstEnvioMail.add(regEnvio);
		}

		if (!pendientes.toString().isEmpty()) {
			sinCronograma = false;
			nomCompleto = "";
			Properties props = System.getProperties();
			props.put("mail.smtp.host", envHost);
			props.put("mail.smtp.user", envUsername);
			props.put("mail.smtp.password", envPassword);
			props.put("mail.smtp.starttls.enable", envStartTls);
			props.put("mail.smtp.port", envPort); // 587 is the port number of yahoo mail
			props.put("mail.smtp.auth", envAuth);
			Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(envUsername, envPassword);
				}
			});
			try {
				Thread.sleep(1500); // 1.5 secs
				Message msg = new MimeMessage(session);
				msg.setFrom(new InternetAddress(envUsername, "Deltanet"));

				lstDestino = alertaService.listaEmails("BATCH-01");
				Iterator<Object> tg = lstDestino != null ? lstDestino.iterator() : null;
				while (tg != null && tg.hasNext()) {
					Object[] fila = (Object[]) tg.next();
					msg.addRecipient(Message.RecipientType.TO, new InternetAddress(String.valueOf(fila[0])));
				}

				msg.setSubject("DELTANET - " + title); // Correo dirigido a supervisor de Outsourcing
				Multipart mp = new MimeMultipart();
				MimeBodyPart htmlPart = new MimeBodyPart();

				String[] lineas = pendientes.toString().split("-");
				StringBuilder cuerpo = new StringBuilder(
						"<html><style>table {border-collapse: collapse;font-family: Tahoma, Geneva, sans-serif;}table td {padding: 15px;}\n");
				cuerpo.append(
						"table thead td {background-color: #54585d;color: #ffffff;font-weight: bold;font-size: 12px;\tborder: 1px solid #54585d;}\n");
				cuerpo.append("table tbody td {color: #636363;font-size: 10px;\tborder: 1px solid #dddfe1;}\n");
				cuerpo.append("table tbody tr {background-color: #f9fafb;text-align:center;}\n");
				cuerpo.append("table tbody tr:nth-child(odd) {background-color: #ffffff;}</style>\n");
				cuerpo.append(
						"<body><p>Estimad@s, \nPor medio de la presente se brinda la relación de los colaboradores outsourcing que "
								+ "tienen pendiente el registro de actividades entre las fechas " + strLunes + " y "
								+ strFecha + "\n "
								+ "</p><p style=\"font-size:10px\"><b>F</b>: Feriado<br>\n<b>C</b>: Compensación</p><table style=\"border-collapse: collapse;font-family: Tahoma, Geneva, sans-serif;\">\n");
				cuerpo.append(
						"<thead><tr><td style=\"text-align: center; background-color: #54585d;color: #ffffff;font-weight: bold;font-size: 12px;\tborder: 1px solid #54585d;\">ID</td><td style=\"text-align: center; background-color: #54585d;color: #ffffff;font-weight: bold;font-size: 12px;\tborder: 1px solid #54585d;\">Colaborador</td>");
				boolean ciclo = true;
				fechaCampo = fecLunes;

				while (ciclo) {
					strFecFld = fmtCampos.format(fechaCampo);
					cuerpo.append(
							"<td style=\"text-align: center; background-color: #54585d;color: #ffffff;font-weight: bold;font-size: 12px;\tborder: 1px solid #54585d;\">"
									+ strFecFld + "</td>");

					fechaCampo = addDays(fechaCampo, 1);
					if (fechaCampo.after(fecViernes))
						ciclo = false;
				}

				cuerpo.append(
						"<td style=\"text-align: center; background-color: #54585d;color: #ffffff;font-weight: bold;font-size: 12px;\tborder: 1px solid #54585d;\">Observación</td></tr></thead>\n");
				cuerpo.append("<tbody>\n");

				for (int i = 0; i < lstEnvioMail.size(); i++) {
					cuerpo.append(
							"<tr style=\"background-color: #f9fafb;text-align:center;\"><td style=\"color: #636363;font-size: 10px;\tborder: 1px solid #dddfe1;\" width=\"5%\">"
									+ lstEnvioMail.get(i).getIdColaborador()
									+ "</td><td style=\"text-align: left; color: #636363;font-size: 10px;\tborder: 1px solid #dddfe1;\" width=\"20%\">"
									+ lstEnvioMail.get(i).getNomCompleto()
									+ "</td><td style=\"color: #636363;font-size: 10px;\tborder: 1px solid #dddfe1;\" width=\"3%\">"
									+ lstEnvioMail.get(i).getHraL()
									+ "</td><td style=\"color: #636363;font-size: 10px;\tborder: 1px solid #dddfe1;\" width=\"3%\">"
									+ lstEnvioMail.get(i).getHraM()
									+ "</td><td style=\"color: #636363;font-size: 10px;\tborder: 1px solid #dddfe1;\" width=\"3%\">"
									+ lstEnvioMail.get(i).getHraX()
									+ "</td><td style=\"color: #636363;font-size: 10px;\tborder: 1px solid #dddfe1;\" width=\"3%\">"
									+ lstEnvioMail.get(i).getHraJ()
									+ "</td><td style=\"color: #636363;font-size: 10px;\tborder: 1px solid #dddfe1;\" width=\"3%\">"
									+ lstEnvioMail.get(i).getHraV()
									+ "</td><td style=\"text-align: left; color: #636363;font-size: 10px;\tborder: 1px solid #dddfe1;\" width=\"40%\">"
									+ lstEnvioMail.get(i).getObservacion() + "</td></tr>");
				}
				cuerpo.append("</tbody></table></body></html>\n");

				// Generar el archivo Excel temporal (sin campo Observación)
				Workbook workbook = new XSSFWorkbook();
				Sheet sheet = workbook.createSheet("Consolidado Horas Semanal");

				// Encabezados
				Row headerRow = sheet.createRow(0);
				headerRow.createCell(0).setCellValue("ID");
				headerRow.createCell(1).setCellValue("Colaborador");
				headerRow.createCell(2).setCellValue("Lunes");
				headerRow.createCell(3).setCellValue("Martes");
				headerRow.createCell(4).setCellValue("Miércoles");
				headerRow.createCell(5).setCellValue("Jueves");
				headerRow.createCell(6).setCellValue("Viernes");

				// Datos
				for (int i = 0; i < lstEnvioMail.size(); i++) {
					lstCroTabla reg = lstEnvioMail.get(i);
					Row row = sheet.createRow(i + 1);
					row.createCell(0).setCellValue(reg.getIdColaborador());
					row.createCell(1).setCellValue(reg.getNomCompleto());
					row.createCell(2).setCellValue(reg.getHraL());
					row.createCell(3).setCellValue(reg.getHraM());
					row.createCell(4).setCellValue(reg.getHraX());
					row.createCell(5).setCellValue(reg.getHraJ());
					row.createCell(6).setCellValue(reg.getHraV());
				}

				// Guardar el archivo temporalmente
				File tempFile = File.createTempFile("Consolidado_Horas_Semanal_" + strFecha, ".xlsx");
				try (FileOutputStream fos = new FileOutputStream(tempFile)) {
					workbook.write(fos);
				}
				workbook.close();

				// Adjuntar el Excel al correo
				MimeBodyPart excelPart = new MimeBodyPart();
				excelPart.attachFile(tempFile);
				excelPart.setFileName("Consolidado_Horas_Semanal_" + strFecha + ".xlsx");
				mp.addBodyPart(excelPart);

				htmlPart.setContent(cuerpo.toString(), "text/html;charset=utf-8");
				mp.addBodyPart(htmlPart);
				msg.setContent(mp);

				// msg.setText(cuerpo.toString());

				Transport.send(msg);

			} catch (Exception e) {
				response.put("msgEmail", "Error el enviar correo a la cuenta: " + email);
				response.put("errEmail", e.getMessage());
			}
		}

		response.put("mensaje", "Se concluyo la ejecucion del servicio");
		return (response);
	}

	// Cron de envio de horas al supervisor CR-BSG-02 // BATCH-01
	@Scheduled(cron = "0 50 23 * * FRI")
	@GetMapping("/emrelouts")
	public ResponseEntity<?> EmailRelOuts() {
		Map<String, Object> response = new HashMap<>();
		Date fecha = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		/*
		 * int diaSem = calendar.get(Calendar.DAY_OF_WEEK); if (diaSem != 6) {
		 * response.put("mensaje",
		 * "La funcionalidad solo se puede ejecutar los viernes"); } else {
		 */
		try {
			response = logReproceso(fecha, "Supervisor - Consolidado de horas semanal");
		} catch (ParseException e) {
			response.put("mensaje", "Se produjo error en el flujo del proceso.");
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		response.put("fecha", calendar.get(Calendar.DAY_OF_WEEK));
		// }
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// Servicio donde se reprocesa
	@GetMapping("/emreloutrep")
	public ResponseEntity<?> EmailRelOuts(
			@RequestParam(required = false, name = "fecini") @DateTimeFormat(pattern = "dd/MM/yyyy") Date fecIni,
			@RequestParam(required = false, name = "fecfin") @DateTimeFormat(pattern = "dd/MM/yyyy") Date fecFin) {
		Map<String, Object> response = new HashMap<>();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

		Calendar calendarL = Calendar.getInstance();
		Calendar calendarV = Calendar.getInstance();
		calendarL.setTime(fecIni);
		String strLunes = formatter.format(calendarL.getTime());
		int diaSem = calendarL.get(Calendar.DAY_OF_WEEK);
		if (diaSem != 2) {
			response.put("mensaje", "La fecha inicial no es Lunes");
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		calendarV.setTime(fecFin);
		diaSem = calendarV.get(Calendar.DAY_OF_WEEK);
		if (diaSem != 6) {
			response.put("mensaje", "La fecha final no es viernes");
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}

		calendarV.add(Calendar.DATE, -4);
		String strCompa = formatter.format(calendarV.getTime());
		if (!strLunes.equals(strCompa)) {
			response.put("mensaje", "El rango de fechas no corresponde a una semana");
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}

		try {
			response = logReproceso(fecFin, "Reproceso - Supervisor - Consolidado de horas semanal");
		} catch (ParseException e) {
			response.put("mensaje", "Se produjo error en el flujo del proceso.");
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// Servicio cron donde cada empleado recive un correo de las horas registradas
	// CR-BSG-01
	@Scheduled(cron = "0 50 23 * * FRI")
	@GetMapping("/listarelouts")
	public ResponseEntity<?> ListaRelOuts() {
		Map<String, Object> response = new HashMap<>();
		Date fecha = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		/*
		 * int diaSem = calendar.get(Calendar.DAY_OF_WEEK); if (diaSem != 6) {
		 * response.put("mensaje",
		 * "La funcionalidad solo se puede ejecutar los viernes"); } else {
		 */
		try {
			response = logColaborador(fecha);
		} catch (ParseException e) {
			response.put("mensaje", "Se produjo error en el flujo del proceso.");
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		response.put("fecha", calendar.get(Calendar.DAY_OF_WEEK));
		/* } */

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/lstreloutrep")
	public ResponseEntity<?> ListaRelOuts(
			@RequestParam(required = false, name = "fecha") @DateTimeFormat(pattern = "dd/MM/yyyy") Date fecha) {
		Map<String, Object> response = new HashMap<>();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		int diaSem = calendar.get(Calendar.DAY_OF_WEEK);
		if (diaSem != 6) {
			response.put("mensaje", "La funcionalidad solo se puede ejecutar los viernes");
		} else {
			try {
				response = logColaborador(fecha);
			} catch (ParseException e) {
				response.put("mensaje", "Se produjo error en el flujo del proceso.");
				response.put("error", e.getMessage());
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			response.put("fecha", calendar.get(Calendar.DAY_OF_WEEK));
		}

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/create")
	public ResponseEntity<?> create(@RequestParam(required = false, name = "idContrato") Long idContrato) {
		Map<String, Object> response = new HashMap<>();
		Contrato2024 contrato = contratoService.findById(idContrato);
		if (contrato == null) {
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		xContrato registro = new xContrato();
		xRelacion regRel = new xRelacion();
		xPersona regPer = new xPersona();

		regPer.setIdPer(contrato.getRelacion().getPersona().getId());
		regPer.setIdTipoPer(contrato.getRelacion().getPersona().getTipoPer().getIdTipoPer());
		regPer.setIdTipoDoc(contrato.getRelacion().getPersona().getTipoDoc().getIdTipDoc());
		regPer.setDocumento(contrato.getRelacion().getPersona().getDocumento());
		regPer.setAtencion(contrato.getRelacion().getPersona().getAtencion().getAbrev());
		regPer.setEstado(contrato.getRelacion().getPersona().getEstado());
		regPer.setVisible(contrato.getRelacion().getPersona().getVisible());
		if (contrato.getRelacion().getPersona().getPerNat() != null) {
			regPer.setIdPerNat(contrato.getRelacion().getPersona().getPerNat().getIdPerNat());
			regPer.setNombre(contrato.getRelacion().getPersona().getPerNat().getNombre() + " "
					+ contrato.getRelacion().getPersona().getPerNat().getApePaterno() + " "
					+ contrato.getRelacion().getPersona().getPerNat().getApeMaterno());
		}
		if (contrato.getRelacion().getPersona().getPerJur() != null) {
			regPer.setIdPerJur(contrato.getRelacion().getPersona().getPerJur().getIdPerJur());
			regPer.setRazSoc(contrato.getRelacion().getPersona().getPerJur().getRazonSocial());
		}
		regPer.setIdTitular(contrato.getRelacion().getPersona().getIdTitular());

		regRel.setIdRel(contrato.getRelacion().getIdRel());
		regRel.setIdTiporel(contrato.getRelacion().getIdTipoRel());
		regRel.setPersona(regPer);
		regRel.setIdArea(contrato.getRelacion().getIdArea());
		regRel.setArea(contrato.getRelacion().getArea());
		regRel.setEstado(contrato.getRelacion().getEstado());
		regRel.setFecIni(contrato.getRelacion().getFecIni());
		regRel.setFecFin(contrato.getRelacion().getFecFin());

		registro.setId(contrato.getId());
		registro.setRelacion(regRel);
		registro.setIdTipoContrato(contrato.getTipoContrato().getId());
		registro.setIdtipoServicio(contrato.getTipoServicio().getId());
		registro.setIdFormaPago(contrato.getFormaPago().getId());
		registro.setDescripcion(contrato.getDescripcion());
		registro.setFecInicio(contrato.getFecInicio());
		registro.setFecFin(contrato.getFecFin());
		registro.setMonto(contrato.getMonto());
		registro.setHes(contrato.getHes());
		registro.setEstado(contrato.getEstado());
		List<CronogramaModalidad> modalidades = this.modalidadService.findActive();
		if (modalidades == null) {
			modalidades = new ArrayList<>();
		}
		response.put("modalidades", modalidades);
		response.put("contrato", registro);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/create")
	public ResponseEntity<?> creacion(@RequestParam(name = "idContrato") Long idContrato,
			@RequestParam(name = "idConsultor") Long idConsultor, @RequestParam(name = "idModalidad") Long idModalidad,
			@RequestParam(name = "descrip") String descrip,
			@RequestParam(name = "periodoIni") @DateTimeFormat(pattern = "dd/MM/yyyy") Date perIni,
			@RequestParam(name = "periodoFin") @DateTimeFormat(pattern = "dd/MM/yyyy") Date perFin,
			@RequestParam(required = false, name = "nroOC") String nroOC,
			@RequestParam(required = false, name = "fechaOC") @DateTimeFormat(pattern = "dd/MM/yyyy") Date fechaOC,
			@RequestParam(name = "hes") int hes, @RequestParam(name = "monto") Double monto,
			@RequestParam(name = "createUser") String createUser) {
		Map<String, Object> response = new HashMap<>();
		if (createUser == null) {
			createUser = "admin";
		}
		Contrato2024 contrato = contratoService.findById(idContrato);
		if (contrato == null) {
			response.put("mensaje", "No existe el contrato enviado.");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		Persona consultor = personaService.buscarId(idConsultor);
		if (consultor == null) {
			response.put("mensaje", "No existe el consultor enviado.");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Validación de cronogramas existentes
		List<Cronograma> cronogramasExistentes = cronogramaService.findByPersonaAndEstado(consultor.getId(), 1L);

		for (Cronograma cronogramaExistente : cronogramasExistentes) {
			// Verificar si hay superposición de fechas
			if (fechasSuperpuestas(cronogramaExistente.getPeriodoInicial(), cronogramaExistente.getPeriodoFinal(),
					perIni, perFin)) {
				response.put("mensaje",
						"El consultor ya tiene un cronograma activo en el rango de fechas especificado. "
								+ "Periodo del cronograma existente: "
								+ formatearFecha(cronogramaExistente.getPeriodoInicial()) + " - "
								+ formatearFecha(cronogramaExistente.getPeriodoFinal()));
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
		}

		Cronograma cronograma = new Cronograma();
		try {
			cronograma.setIdContrato(contrato.getId());
			cronograma.setIdModalidad(idModalidad);
			cronograma.setPersona(consultor);
			cronograma.setDescripcion(descrip);
			cronograma.setPeriodoInicial(perIni);
			cronograma.setPeriodoFinal(perFin);
			cronograma.setNroOC(nroOC);
			cronograma.setFechaOC(fechaOC);
			cronograma.setHes(hes);
			cronograma.setMonto(monto);
			cronograma.setEstado(3L); // 3:creado
			cronograma.setCreateUser(createUser);
			cronograma.setCreateDate(new Date());

			cronogramaService.save(cronograma);
		} catch (Exception e) {
			response.put("mensaje", "Se produjo un error en la creacion del cronograma.");
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "Se creo el cronograma satisfactoriamente.");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	private boolean fechasSuperpuestas(Date inicio1, Date fin1, Date inicio2, Date fin2) {
		return (inicio2.compareTo(fin1) <= 0 && inicio2.compareTo(inicio1) >= 0)
				|| (fin2.compareTo(fin1) <= 0 && fin2.compareTo(inicio1) >= 0)
				|| (inicio1.compareTo(fin2) <= 0 && inicio1.compareTo(inicio2) >= 0)
				|| (fin1.compareTo(fin2) <= 0 && fin1.compareTo(inicio2) >= 0);
	}

	private String formatearFecha(Date fecha) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(fecha);
	}

	@GetMapping("/show")
	public ResponseEntity<?> verReg(@RequestParam(name = "idCronograma") Long idCronograma) {
		Map<String, Object> response = new HashMap<>();
		Cronograma cronograma = cronogramaService.findById(idCronograma);
		if (cronograma == null) {
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		List<CronogramaModalidad> modalidades = this.modalidadService.findActive();
		if (modalidades == null) {
			modalidades = new ArrayList<>();
		}
		response.put("modalidades", modalidades);
		response.put("cronograma", cronograma);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/updEstado")
	public ResponseEntity<?> updateEstado(@RequestParam(name = "idCronograma") Long idCronograma,
			@RequestParam(name = "estado") Long estado) {
		/*
		 * Estados: 1: Inactivo (Solo puede cambiar a 2,3 ó 4) 2: Activo (Solo puede
		 * cambiar a 1 ó 4) 3: Creado (Solo puede cambiar a 1 ó 2) 4: Finalizado (No
		 * puede cambiar de estado)
		 */
		Map<String, Object> response = new HashMap<>();

		// Validar que existe el cronograma
		Cronograma cronograma = cronogramaService.findById(idCronograma);
		if (cronograma == null) {
			response.put("mensaje", "El id del cronograma enviado no fue encontrado.");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		Long actEstado = cronograma.getEstado();
		Long newEstado = 0L;

		// Si se está activando el cronograma (pasando de estado 3 a 1)
		if (actEstado == 3 && estado == 1) {
			// Buscar cronogramas activos SOLO del mismo consultor
			List<Cronograma> cronogramasActivosConsultor = cronogramaService
					.findByPersonaAndEstado(cronograma.getPersona().getId(), 1L);

			for (Cronograma cronoActivo : cronogramasActivosConsultor) {
				if (fechasSuperpuestas(cronograma.getPeriodoInicial(), cronograma.getPeriodoFinal(),
						cronoActivo.getPeriodoInicial(), cronoActivo.getPeriodoFinal())) {

					response.put("mensaje", String.format(
							"No se puede activar el cronograma. Las fechas (%s - %s) se superponen con el cronograma activo: %s (ID: %d) del mismo consultor con fechas (%s - %s)",
							formatearFecha(cronograma.getPeriodoInicial()),
							formatearFecha(cronograma.getPeriodoFinal()), cronoActivo.getDescripcion(),
							cronoActivo.getId(), formatearFecha(cronoActivo.getPeriodoInicial()),
							formatearFecha(cronoActivo.getPeriodoFinal())));
					return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
				}
			}
			newEstado = 1L;
		}
		// Validación de estados existente
		else if (actEstado == 3) {
			if (estado == 2) {
				newEstado = estado;
			} else {
				response.put("mensaje", "El cronograma tiene estado 3, se espera un nuevo estado 1 ó 2");
				return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else if (actEstado == 1) {
			if (estado == 2 || estado == 3 || estado == 4) {
				newEstado = estado;
			} else {
				response.put("mensaje", "El cronograma tiene estado 1, se espera un nuevo estado 2, 3 ó 4");
				return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else if (actEstado == 2) {
			if (estado == 1 || estado == 4) {
				newEstado = estado;
			} else {
				response.put("mensaje", "El cronograma tiene estado 2, se espera un nuevo estado 1 ó 4");
				return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else if (actEstado == 4) {
			response.put("mensaje", "El cronograma tiene estado 4, no se puede cambiar el estado.");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Guardar histórico
		Long idHis;
		try {
			AdmCronogramaHist reg = new AdmCronogramaHist();
			reg.setIdCrono(cronograma.getId());
			reg.setIdContrato(cronograma.getIdContrato());
			reg.setIdModalidad(cronograma.getIdModalidad());
			reg.setIdConsultor(cronograma.getPersona().getId());
			reg.setDescripcion(cronograma.getDescripcion());
			reg.setPeriodoInicial(cronograma.getPeriodoInicial());
			reg.setPeriodoFinal(cronograma.getPeriodoFinal());
			reg.setNroOc(cronograma.getNroOC());
			reg.setFechaOc(cronograma.getFechaOC());
			reg.setHes(cronograma.getHes());
			reg.setMonto(cronograma.getMonto());
			reg.setEstado(cronograma.getEstado());
			reg.setCreateUser(cronograma.getCreateUser());
			reg.setCreateDate(cronograma.getCreateDate());
			reg.setUpdateUser(cronograma.getUpdateUser());
			reg.setUpdateDate(cronograma.getUpdateDate());
			cronoHisService.save(reg);
			idHis = reg.getId();
		} catch (Exception e) {
			response.put("mensaje", "Se produjo un error en la actualización del histórico del cronograma.");
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Actualizar estado
		cronograma.setEstado(newEstado);
		cronograma.setUpdateUser("admin");
		cronograma.setUpdateDate(new Date());

		try {
			cronogramaService.save(cronograma);
		} catch (Exception e) {
			cronoHisService.delete(idHis);
			response.put("mensaje", "Se produjo un error en la actualización del estado del cronograma.");
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "Se actualizó correctamente el estado del cronograma.");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/updCron")
	public ResponseEntity<?> updCron(@RequestParam(name = "idCronograma") Long idCronograma,
			@RequestParam(required = false, name = "idContrato") Long idContrato,
			@RequestParam(required = false, name = "idModalidad") Long idModalidad,
			@RequestParam(required = false, name = "idConsultor") Long idConsultor,
			@RequestParam(required = false, name = "descrip") String descrip,
			@RequestParam(required = false, name = "periodoIni") @DateTimeFormat(pattern = "dd/MM/yyyy") Date perIni,
			@RequestParam(required = false, name = "periodoFin") @DateTimeFormat(pattern = "dd/MM/yyyy") Date perFin,
			@RequestParam(required = false, name = "nroOC") String nroOC,
			@RequestParam(required = false, name = "fechaOC") @DateTimeFormat(pattern = "dd/MM/yyyy") Date fechaOC,
			@RequestParam(required = false, name = "hes") int hes,
			@RequestParam(required = false, name = "monto") Double monto,
			@RequestParam(required = false, name = "updUser") String updUser) {
		Map<String, Object> response = new HashMap<>();
		boolean datos = false;
		if (updUser == null) {
			updUser = "admin";
		}
		Cronograma cronograma = cronogramaService.findById(idCronograma);
		if (cronograma == null) {
			response.put("mensaje", "El id del cronograma enviado no fue encontrado.");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (idConsultor != null) {
			// Verificar que el consultor existe
			Persona consultor = personaService.buscarId(idConsultor);
			if (consultor == null) {
				response.put("mensaje", "El id del consultor enviado no fue encontrado.");
				return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}

			// Usar las fechas proporcionadas o las existentes
			Date fechaInicio = perIni != null ? perIni : cronograma.getPeriodoInicial();
			Date fechaFin = perFin != null ? perFin : cronograma.getPeriodoFinal();

			// Obtener cronogramas activos del nuevo consultor
			List<Cronograma> cronogramasExistentes = cronogramaService.findByPersonaAndEstado(idConsultor, 1L);

			// Verificar superposición con otros cronogramas
			for (Cronograma cronogramaExistente : cronogramasExistentes) {
				if (!cronogramaExistente.getId().equals(idCronograma) && // Excluir el cronograma actual
						fechasSuperpuestas(cronogramaExistente.getPeriodoInicial(),
								cronogramaExistente.getPeriodoFinal(), fechaInicio, fechaFin)) {
					response.put("mensaje",
							String.format(
									"El consultor ya tiene un cronograma activo en el rango de fechas %s - %s. "
											+ "No se puede asignar el periodo %s - %s",
									formatearFecha(cronogramaExistente.getPeriodoInicial()),
									formatearFecha(cronogramaExistente.getPeriodoFinal()), formatearFecha(fechaInicio),
									formatearFecha(fechaFin)));
					return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
				}
			}
			cronograma.setPersona(consultor);
			datos = true;
		}
		// Si solo se cambian las fechas, validar con el consultor actual
		else if (perIni != null || perFin != null) {
			Date fechaInicio = perIni != null ? perIni : cronograma.getPeriodoInicial();
			Date fechaFin = perFin != null ? perFin : cronograma.getPeriodoFinal();

			// Obtener cronogramas activos del consultor actual
			List<Cronograma> cronogramasExistentes = cronogramaService
					.findByPersonaAndEstado(cronograma.getPersona().getId(), 1L);

			// Verificar superposición con otros cronogramas
			for (Cronograma cronogramaExistente : cronogramasExistentes) {
				if (!cronogramaExistente.getId().equals(idCronograma) && // Excluir el cronograma actual

						fechasSuperpuestas(cronogramaExistente.getPeriodoInicial(),
								cronogramaExistente.getPeriodoFinal(), fechaInicio, fechaFin)) {
					response.put("mensaje",
							String.format(
									"El consultor ya tiene un cronograma activo en el rango de fechas %s - %s. "
											+ "No se puede asignar el periodo %s - %s",
									formatearFecha(cronogramaExistente.getPeriodoInicial()),
									formatearFecha(cronogramaExistente.getPeriodoFinal()), formatearFecha(fechaInicio),
									formatearFecha(fechaFin)));
					return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
				}
			}
			if (perIni != null) {
				cronograma.setPeriodoInicial(perIni);
				datos = true;
			}
			if (perFin != null) {
				cronograma.setPeriodoFinal(perFin);
				datos = true;
			}
		}

		if (idContrato != null) {
			cronograma.setIdContrato(idContrato);
			datos = true;
		}
		if (idModalidad != null) {
			cronograma.setIdModalidad(idModalidad);
			datos = true;
		}
		if (idConsultor != null) {
			Persona consultor = personaService.buscarId(idConsultor);
			if (consultor == null) {
				response.put("mensaje", "El id del consultor enviado no fue encontrado.");
				return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			cronograma.setPersona(consultor);
			datos = true;
		}
		if (descrip != null) {
			cronograma.setDescripcion(descrip);
			datos = true;
		}
		if (perIni != null) {
			cronograma.setPeriodoInicial(perIni);
			datos = true;
		}
		if (perFin != null) {
			cronograma.setPeriodoFinal(perFin);
			datos = true;
		}
		if (nroOC != null) {
			cronograma.setNroOC(nroOC);
			datos = true;
		}
		if (fechaOC != null) {
			cronograma.setFechaOC(fechaOC);
			datos = true;
		}
		cronograma.setHes(hes);
		if (monto != null) {
			cronograma.setMonto(monto);
			datos = true;
		}
		if (!datos) {
			response.put("mensaje", "No se tiene datos para actualizar el cronograma.");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		boolean his = false;
		Long idHis = 0L;

		if (cronograma.getIdModalidad() != idModalidad || cronograma.getPersona().getId() != idConsultor
				|| cronograma.getPeriodoInicial() != perIni || cronograma.getPeriodoFinal() != perFin
				|| cronograma.getFechaOC() != fechaOC || cronograma.getMonto() != monto) {
			his = true;
			try {
				AdmCronogramaHist reg = new AdmCronogramaHist();
				reg.setIdCrono(cronograma.getId());
				reg.setIdContrato(cronograma.getIdContrato());
				reg.setIdModalidad(cronograma.getIdModalidad());
				reg.setIdConsultor(cronograma.getPersona().getId());
				reg.setDescripcion(cronograma.getDescripcion());
				reg.setPeriodoInicial(cronograma.getPeriodoInicial());
				reg.setPeriodoFinal(cronograma.getPeriodoFinal());
				reg.setNroOc(cronograma.getNroOC());
				reg.setFechaOc(cronograma.getFechaOC());
				reg.setHes(cronograma.getHes());
				reg.setMonto(cronograma.getMonto());
				reg.setEstado(cronograma.getEstado());
				reg.setCreateUser(cronograma.getCreateUser());
				reg.setCreateDate(cronograma.getCreateDate());
				reg.setUpdateUser(cronograma.getUpdateUser());
				reg.setUpdateDate(cronograma.getUpdateDate());
				cronoHisService.save(reg);
				idHis = reg.getId();
			} catch (Exception e) {
				response.put("mensaje", "Se produjo un error en la actualizacion del historico del cronograma.");
				response.put("error", e.getMessage());
				return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		cronograma.setUpdateUser(updUser);
		cronograma.setUpdateDate(new Date());

		try {
			cronogramaService.save(cronograma);
		} catch (Exception e) {
			if (his)
				cronoHisService.delete(idHis);
			response.put("mensaje", "Se produjo un error en la actualizacion del cronograma.");
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "Se actualizó correctamente el contrato.");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/busCron")
	public ResponseEntity<?> busCron(@RequestParam(required = false, name = "idContrato") Long idContrato,
			@RequestParam(required = false, name = "descrip") String descrip,
			@RequestParam(required = false, name = "idConsultor") Long idConsultor,
			@RequestParam(required = false, name = "periodoIni") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecIni,
			@RequestParam(required = false, name = "periodoFin") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecFin,
			@RequestParam(required = false, name = "nroOC") String nroOC,
			@RequestParam(required = false, name = "fechaOC") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaOC,
			@RequestParam(required = false, name = "estado") Long estado,
			@RequestParam(required = false, name = "numDias") Integer nDias,
			@RequestParam(value = "paginado", required = false, defaultValue = "0") Integer swPag,
			@RequestParam(value = "column", required = false) Integer column,
			@RequestParam(value = "dir", required = false) String dir, Pageable pageable) throws ParseException {
		Map<String, Object> response = new HashMap<>();

		String strEtiqueta = "CONTRATO_COM_ALERTA_VCMTO";
		SisParam param = parametroService.buscaEtiqueta(strEtiqueta);
		if (param == null) {
			response.put("mensaje", "no se encuentra el parametro: [" + strEtiqueta + "]");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		try {
			nDias = Integer.valueOf(param.getValor());
		} catch (Exception x) {
			response.put("mensaje", "el parametro: [" + strEtiqueta + "] no es un numero valido");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Date fecha = new Date();

		Calendar c = Calendar.getInstance();
		String porVencer = "---";
		boolean vencido;
		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
		lstCrono4 lista;
		if (fecIni != null || fecFin != null) {
			if (fecIni == null) {
				response.put("mensaje", "Se espera la fecha de inicio, para completar el rango de fechas de búsqueda.");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			if (fecFin == null) {
				response.put("mensaje", "Se espera la fecha de fin, para completar el rango de fechas de búsqueda.");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
		}

		if (swPag == 0) {
			List<lstCrono4> lstFinal = cronogramaService.listadoFiltrado(idContrato, descrip, idConsultor, fecIni,
					fecFin, nroOC, fechaOC, estado, nDias);
			return ResponseEntity.ok(lstFinal);
		} else {
			Page<lstCrono4> lstFinal = cronogramaService.listadoFiltrado(idContrato, descrip, idConsultor, fecIni,
					fecFin, nroOC, fechaOC, estado, nDias, column, dir, pageable);
			response.put("datos", lstFinal.getContent());
			response.put("totRegs", lstFinal.getTotalElements());
			response.put("totPags", lstFinal.getTotalPages());
			return ResponseEntity.ok(response);
		}
	}

	@GetMapping("/listaTablas")
	public ResponseEntity<?> lstTablas() {
		Map<String, Object> response = new HashMap<>();

		List<Object> lstRecursos = relacionService.listaPersonal();

		response.put("personal", lstRecursos);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/listaRecursos")
	public ResponseEntity<?> lstRecursos() {
		Map<String, Object> response = new HashMap<>();

		List<Object> lstRecursos = cronogramaService.listaRecursos();

		response.put("consultores", lstRecursos);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// Cron de envio de horas a el supervisor CR-BSG-0X // BATCH-02
	@Scheduled(cron = "0 50 23 * * THU")
	@GetMapping("/listareloutsJueves")
	public ResponseEntity<?> relacionOut() {
		Map<String, Object> response = new HashMap<>();
		Date fecha = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		try {
			response = viewProceso(fecha, "Supervisor - Consolidado de actividades semanal");
		} catch (ParseException e) {
			response.put("message", "Se produjo error en el flujo del proceso.");
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		response.put("fecha", calendar.get(Calendar.DAY_OF_WEEK));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	public Map<String, Object> viewProceso(Date fecha, String title) throws ParseException {
		Map<String, Object> response = new HashMap<>();
		List<Object> feriados = null;
		List<Object> compensaciones = null;
		List<Object> lstRegHoras = null;
		List<Map<String, String>> listaReporteVertical = new ArrayList<>();
		int orden = 1;

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		SimpleDateFormat diaSemanaFmt = new SimpleDateFormat("EEEE", new Locale("es", "ES"));

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		String strFecha = formatter.format(calendar.getTime());
		Date fecJueves = formatter.parse(strFecha);

		calendar.set(Calendar.DAY_OF_MONTH, 1);
		String strViernes = formatter.format(calendar.getTime());
		Date fecViernes = formatter.parse(strViernes);

		feriados = feriadoService.buscaRangoFecha(fecViernes, fecJueves);

		List<Object> listado = cronogramaService.listaRelacionOutsourcing();
		Iterator<Object> it = listado != null ? listado.iterator() : null;

		while (it != null && it.hasNext()) {
			Object[] col = (Object[]) it.next();
			int idPer = ((Long) col[1]).intValue();
			String nomCompleto = col[2] + " " + col[3] + " " + col[4];

			Persona per = null;
			Contrato2024 contract = contratoService.findByRelacion2((Long) col[0]);
			if (contract != null) {
				per = perService.buscarId(contract.getIdCliente());
			} else {
				continue;
			}

			compensaciones = regActividadService.BuscarPorRangoFechas((Long) col[1], fecViernes, fecJueves, 8L);
			lstRegHoras = regHorasService.findByPersonaCronogramaFechaEstado2(idPer, fecViernes, fecha, 1);

			Map<Date, List<String>> actividadesPorDia = new HashMap<>();
			if (lstRegHoras != null) {
				for (Object reg : lstRegHoras) {
					Object[] colReg = (Object[]) reg;
					Date diaRegistro = (Date) colReg[0];
					int horas = ((Number) colReg[1]).intValue();
					String actividad = (String) colReg[2];
					actividadesPorDia.computeIfAbsent(diaRegistro, k -> new ArrayList<>()).add(actividad);
				}
			}

			Date fReg = fecViernes;
			calendar.setTime(fReg);
			boolean ciclo = true;

			while (ciclo) {
				Date currentFReg = fReg;
				Map<String, String> fila = new HashMap<>();
				fila.put("ID", String.valueOf(orden++));
				fila.put("Cliente", per.getPerJur().getRazonComercial());
				fila.put("Colaborador", nomCompleto);
				SimpleDateFormat formatoFechaCorto = new SimpleDateFormat("dd/MM/yyyy");
				fila.put("Fecha", formatoFechaCorto.format(currentFReg));
				String diaSemanaStr = diaSemanaFmt.format(currentFReg).toUpperCase();
				String inicialDia = diaSemanaStr.substring(0, 1);
				if (diaSemanaStr.startsWith("MIÉRCOLES")) {
					fila.put("Día Semana", "X");
				} else {
					fila.put("Día Semana", inicialDia);
				}

				String actividadPrincipal = "";
				String actividadRealizada = "";

				boolean compensacion = buscaFecha(compensaciones, currentFReg);
				boolean asueto = buscaFecha(feriados, currentFReg);

				List<String> actividades = actividadesPorDia.get(currentFReg);
				Calendar calDia = Calendar.getInstance();
				calDia.setTime(currentFReg);
				int diaSemana = calDia.get(Calendar.DAY_OF_WEEK);
				if (actividades != null && !actividades.isEmpty()) {
					for (String actividad : actividades) {
						Map<String, String> filaActividad = new HashMap<>(fila);
						filaActividad.put("ID", String.valueOf(orden++));
						filaActividad.put("ActividadPrincipal", actividad.trim());
						filaActividad.put("ActividadRealizada", actividad.trim());
						listaReporteVertical.add(filaActividad);
					}
					if (diaSemana == Calendar.SATURDAY || diaSemana == Calendar.SUNDAY) {
						Map<String, String> filaFinSemana = new HashMap<>(fila);
						filaFinSemana.put("ID", String.valueOf(orden++));
						filaFinSemana.put("ActividadPrincipal", "FIN DE SEMANA");
						filaFinSemana.put("ActividadRealizada", "FIN DE SEMANA");
						listaReporteVertical.add(filaFinSemana);
					}
				}
				if (asueto) {
					Optional<Object[]> feriado = feriados.stream().map(o -> (Object[]) o)
							.filter(o -> formatter.format((Date) o[0]).equals(formatter.format(currentFReg)))
							.findFirst();
					String descripcionFeriado = feriado.map(f -> String.valueOf(f[1])).orElse("Feriado");
					actividadPrincipal = descripcionFeriado.trim();
					actividadRealizada = descripcionFeriado.trim();
					Map<String, String> filaAsueto = new HashMap<>(fila);
					filaAsueto.put("ID", String.valueOf(orden++));
					filaAsueto.put("ActividadPrincipal", actividadPrincipal.trim());
					filaAsueto.put("ActividadRealizada", actividadRealizada.trim());
					listaReporteVertical.add(filaAsueto);
				} else if (actividades == null || actividades.isEmpty()) {
					Map<String, String> filaSinRegistro = new HashMap<>(fila);
					filaSinRegistro.put("ID", String.valueOf(orden++));
					if (diaSemana == Calendar.SATURDAY || diaSemana == Calendar.SUNDAY) {
						actividadPrincipal = "FIN DE SEMANA";
						actividadRealizada = "FIN DE SEMANA";
					} else {
						actividadPrincipal = "NO LLENÓ INFORMACIÓN";
						actividadRealizada = "NO LLENÓ INFORMACIÓN";
					}
					filaSinRegistro.put("ActividadPrincipal", actividadPrincipal.trim());
					filaSinRegistro.put("ActividadRealizada", actividadRealizada.trim());
					listaReporteVertical.add(filaSinRegistro);
				}

				calendar.setTime(currentFReg);
				calendar.add(Calendar.DAY_OF_MONTH, 1);
				fReg = calendar.getTime();
				if (fReg.after(fecJueves))
					ciclo = false;
			}
		}
		int idCorrelativoFinal = 1;
		for (Map<String, String> fila : listaReporteVertical) {
			fila.put("ID", String.valueOf(idCorrelativoFinal++));
		}

		try {
			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet("Reporte Vertical Actividades");
			String[] headers = { "ID", "Cliente", "Colaborador", "Fecha", "Día de la semana", "Actividad Principal",
					"Actividad Realizada" };
			Row headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				headerRow.createCell(i).setCellValue(headers[i]);
			}
			int rowNum = 1;
			// Crear estilo centrado
			CellStyle styleCenter = workbook.createCellStyle();
			styleCenter.setAlignment(HorizontalAlignment.CENTER);
			// Para que el ID sea correlativo
			int idCorrelativo = 1;
			for (Map<String, String> fila : listaReporteVertical) {
				Row row = sheet.createRow(rowNum++);
				Cell cell0 = row.createCell(0);
				cell0.setCellValue(idCorrelativo++);
				cell0.setCellStyle(styleCenter);
				Cell cell1 = row.createCell(1);
				cell1.setCellValue(fila.get("Cliente"));
				cell1.setCellStyle(styleCenter);
				Cell cell2 = row.createCell(2);
				cell2.setCellValue(fila.get("Colaborador"));
				Cell cell3 = row.createCell(3);
				cell3.setCellValue(fila.get("Fecha"));
				cell3.setCellStyle(styleCenter);
				Cell cell4 = row.createCell(4);
				cell4.setCellValue(fila.get("Día Semana"));
				cell4.setCellStyle(styleCenter);
				row.createCell(5).setCellValue(fila.get("ActividadPrincipal"));
				row.createCell(6).setCellValue(fila.get("ActividadRealizada"));
			}
			for (int i = 0; i < headers.length; i++) {
				sheet.autoSizeColumn(i);
			}

			File tempFile = File.createTempFile("ReporteActividades", ".xlsx");
			FileOutputStream fileOut = new FileOutputStream(tempFile);
			workbook.write(fileOut);
			fileOut.close();
			workbook.close();

			Properties props = System.getProperties();
			props.put("mail.smtp.host", envHost);
			props.put("mail.smtp.user", envUsername);
			props.put("mail.smtp.password", envPassword);
			props.put("mail.smtp.starttls.enable", envStartTls);
			props.put("mail.smtp.port", envPort);
			props.put("mail.smtp.auth", envAuth);

			Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(envUsername, envPassword);
				}
			});

			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(envUsername, "Deltanet"));
			lstDestino = alertaService.listaEmails("BATCH-02");
			if (lstDestino != null) {
				for (Object obj : lstDestino) {
					Object[] fila = (Object[]) obj;
					msg.addRecipient(Message.RecipientType.TO, new InternetAddress(String.valueOf(fila[0])));
				}
			}

			msg.setSubject("Deltanet - " + title);
			Multipart mp = new MimeMultipart();

			StringBuilder cuerpo = new StringBuilder("<html><body><table border='1'><tr>");
			for (String h : headers)
				cuerpo.append("<th>").append(h).append("</th>");
			cuerpo.append("</tr>");
			for (Map<String, String> fila : listaReporteVertical) {
				cuerpo.append("<tr>");
				cuerpo.append("<td>").append(fila.getOrDefault("ID", "")).append("</td>");
				cuerpo.append("<td>").append(fila.getOrDefault("Cliente", "")).append("</td>");
				cuerpo.append("<td>").append(fila.getOrDefault("Colaborador", "")).append("</td>");
				cuerpo.append("<td>").append(fila.getOrDefault("Fecha", "")).append("</td>");
				cuerpo.append("<td>").append(fila.getOrDefault("Día Semana", "")).append("</td>");
				cuerpo.append("<td>").append(fila.getOrDefault("ActividadPrincipal", "")).append("</td>");
				cuerpo.append("<td>").append(fila.getOrDefault("ActividadRealizada", "")).append("</td>");
				cuerpo.append("</tr>");
			}
			cuerpo.append("</table></body></html>");

			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(cuerpo.toString(), "text/html;charset=utf-8");
			mp.addBodyPart(htmlPart);

			MimeBodyPart excelPart = new MimeBodyPart();
			excelPart.attachFile(tempFile);
			excelPart.setFileName("Reporte_de_actividades_" + strFecha + ".xlsx");
			mp.addBodyPart(excelPart);
			msg.setContent(mp);

			Transport.send(msg);
			response.put("mensaje", "Correo enviado exitosamente");

			if (tempFile.exists()) {
				tempFile.delete();
			}

		} catch (Exception e) {
			response.put("msgEmail", "Error en el envío: " + e.getMessage());
		}

		if (!response.containsKey("mensaje")) {
			response.put("mensaje", "Se concluyó la ejecución del servicio");
		}
		return response;
	}
}
