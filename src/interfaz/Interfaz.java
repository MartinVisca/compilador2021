package interfaz;

//import assembler.Assembler;
//import de todas las clases que se precisen

import analizadorLexico.AnalizadorLexico;
import analizadorLexico.Token;
import analizadorSintactico.Parser;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

public class Interfaz {
	private JFrame frmCompiler;
	private JTextArea textArea;
	private JTextArea lines;
	private JTextArea problems;
	private JTextArea console;
	private DefaultTableModel structDtm;
	private DefaultTableModel dtm;
	private DefaultTableModel tableDtm;
	private JTable table_Tok;
	//private Assembler assembler = null;

	private String namefile="untitled";

	private static final Color yellow = new Color(255,210,55);
	private static final Color red = new Color(255,120,104);
	Highlighter.HighlightPainter painterLines = new DefaultHighlighter.DefaultHighlightPainter(Color.RED);
	Highlighter.HighlightPainter painterTextArea_red = new DefaultHighlighter.DefaultHighlightPainter(red);
	Highlighter.HighlightPainter painterTextArea_yellow = new DefaultHighlighter.DefaultHighlightPainter(yellow);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Interfaz window = new Interfaz();
					window.frmCompiler.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Interfaz() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmCompiler = new JFrame();
		//frmCompiler.setIconImage(Toolkit.getDefaultToolkit().getImage(Interfaz.class.getResource("/icons/genericregister_obj.gif")));
		frmCompiler.setTitle("Compiler1.0");
		frmCompiler.setBounds(100, 100, 980, 628);
		frmCompiler.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCompiler.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel panel = new JPanel();
		frmCompiler.getContentPane().add(panel);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		// -------------------------------- PANEL PROBLEMS ------------------------------------------
		
		JPanel Problems = new JPanel();
		//tabbedPane.addTab("Problems", new ImageIcon(Interfaz.class.getResource("/icons/errorwarning_tab.gif")), Problems, null);
		
		JScrollPane scrollPane_Problems = new JScrollPane();
		scrollPane_Problems.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		problems = new JTextArea();
		problems.setEditable(false);
		scrollPane_Problems.setViewportView(problems);
		GroupLayout gl_Problems = new GroupLayout(Problems);
		gl_Problems.setHorizontalGroup(
			gl_Problems.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane_Problems, GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
		);
		gl_Problems.setVerticalGroup(
			gl_Problems.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_Problems.createSequentialGroup()
					.addComponent(scrollPane_Problems, GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
					.addGap(0))
		);
		Problems.setLayout(gl_Problems);
		
		// -------------------------------- PANEL CONSOLE ------------------------------------------
		
		JPanel Console = new JPanel();
		//tabbedPane.addTab("Console", new ImageIcon(Interfaz.class.getResource("/icons/console_view.gif")), Console, null);
		
		JScrollPane scrollPane_Console = new JScrollPane();
		scrollPane_Console.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		console = new JTextArea();
		console.setEditable(false);
		scrollPane_Console.setViewportView(console);
		GroupLayout gl_Console = new GroupLayout(Console);
		gl_Console.setHorizontalGroup(
			gl_Console.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane_Console, GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
		);
		gl_Console.setVerticalGroup(
			gl_Console.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_Console.createSequentialGroup()
					.addComponent(scrollPane_Console, GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
					.addGap(0))
		);
		Console.setLayout(gl_Console);
		
		// -------------------------------- PANEL TOKENS ------------------------------------------
		
		JPanel Tokens = new JPanel();
		tabbedPane.addTab("Tokens", null, Tokens, null);
		JScrollPane scrollPane_Tokens = new JScrollPane();
		scrollPane_Tokens.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		String[] colums={"ID","Tipo","Lexema","Linea"};		
		dtm = new DefaultTableModel(null,colums);
		table_Tok = new JTable(dtm);
		scrollPane_Tokens.setViewportView(table_Tok);
		GroupLayout gl_Tokens = new GroupLayout(Tokens);
		gl_Tokens.setHorizontalGroup(
			gl_Tokens.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane_Tokens, GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
		);
		gl_Tokens.setVerticalGroup(
			gl_Tokens.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane_Tokens, GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
		);
		Tokens.setLayout(gl_Tokens);
	// -------------------------------- PANEL ESTRUCTURAS ------------------------------------------
		JPanel Estructuras = new JPanel();
		tabbedPane.addTab("Estructuras", null, Estructuras, null);
		JScrollPane scrollPane_Estructuras= new JScrollPane();
		scrollPane_Estructuras.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		String[] strucColumns={"Linea","Tipo","Descripcion"};
		structDtm=new DefaultTableModel(null,strucColumns);
		JTable table_Estruc= new JTable(structDtm);
		table_Estruc.setAutoCreateRowSorter(true);
		scrollPane_Estructuras.setViewportView(table_Estruc);
		GroupLayout gl_Estructuras = new GroupLayout(Estructuras);
		gl_Estructuras.setHorizontalGroup(
				gl_Estructuras.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane_Estructuras, GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
		);
		gl_Estructuras.setVerticalGroup(
			gl_Estructuras.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane_Estructuras, GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
		);
		Estructuras.setLayout(gl_Estructuras);
	// -------------------------------- PANEL TABLA SIMBOLOS ------------------------------------------

		JPanel tablaSimbolos = new JPanel();
		tabbedPane.addTab("Tabla Simbolos", null, tablaSimbolos, null);
		JScrollPane scrollPane_TS= new JScrollPane();
		scrollPane_TS.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		String[] tableColumns={"Tipo","Nombre","Cant referencias","Uso","�mbito"};
		tableDtm = new DefaultTableModel(null,tableColumns);
		JTable table_TS = new JTable(tableDtm);
		scrollPane_TS.setViewportView(table_TS);
		GroupLayout gl_TS = new GroupLayout(tablaSimbolos);
		gl_TS.setHorizontalGroup(
				gl_TS.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane_TS, GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
		);
		gl_TS.setVerticalGroup(
				gl_TS.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane_TS, GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
		);
		tablaSimbolos.setLayout(gl_TS);
	
		//-------------------------------------- TEXT AREA -----------------------------------------
		
		JScrollPane scrollPane = new JScrollPane();
		
		textArea = new JTextArea();
		textArea.setBackground(Color.WHITE);
		textArea.setFont(new Font("Monospaced", Font.BOLD, 17));
		textArea.setEnabled(false);

		//--------------------------------------MENU BAR -------------------------------------------
		JMenuBar menuBar = new JMenuBar();
		frmCompiler.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem triggerOpenFile = new JMenuItem("Open File");
		//triggerOpenFile.setIcon(new ImageIcon(Interfaz.class.getResource("/icons/file_obj.gif")));
		mnFile.add(triggerOpenFile);
		
		JSeparator separator = new JSeparator();
		mnFile.add(separator);
		
		JMenuItem mntmRun = new JMenuItem("Run");
		//mntmRun.setIcon(new ImageIcon(Interfaz.class.getResource("/icons/lrun_obj.gif")));
		mnFile.add(mntmRun);
		
		JSeparator separator_1 = new JSeparator();
		mnFile.add(separator_1);
		
		JMenuItem triggerExit = new JMenuItem("Exit");
		mnFile.add(triggerExit);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem triggerHowToUse = new JMenuItem("How to use?");
		//triggerHowToUse.setIcon(new ImageIcon(Interfaz.class.getResource("/icons/help_contents.gif")));
		mnHelp.add(triggerHowToUse);
		
		JMenuItem triggerAbout = new JMenuItem("About");
		mnHelp.add(triggerAbout);
		
		//------------------------------------------- NUMERO DE LINEAS -----------------------------------
		lines = new JTextArea("1");
		lines.setFont(new Font("Monospaced", Font.BOLD, 17));
		 
		lines.setBackground(Color.LIGHT_GRAY);
		lines.setEditable(false);
 
		
		/** LINE NUMBERING
		 * Add Listener al area de escritura (textArea).
		 * Por c/ change, insert y remove en el area de escritura el listener escribe en la column que muestra el nro de linea. 
		 */
		textArea.getDocument().addDocumentListener(new DocumentListener(){
			public String getText(){
				int caretPosition = textArea.getDocument().getLength();
				Element root = textArea.getDocument().getDefaultRootElement();
				String text = "1" + System.getProperty("line.separator");
				for(int i = 2; i < root.getElementIndex( caretPosition ) + 2; i++){
					text += i + System.getProperty("line.separator");
				}
				return text;
			}
			@Override
			public void changedUpdate(DocumentEvent de) {
				lines.setText(getText());
			}
 
			@Override
			public void insertUpdate(DocumentEvent de) {
				lines.setText(getText());
			}
 
			@Override
			public void removeUpdate(DocumentEvent de) {
				lines.setText(getText());
			}
 
		});
		scrollPane.setViewportView(textArea);
		scrollPane.setRowHeaderView(lines);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			
		//------------------------------------------ BOTONES ---------------------------------------
		/**
		 * ----------- BOTON ARBOL ---------------
		 */
		
		final JButton btnArbol = new JButton("Tree");
		btnArbol.setEnabled(false);
		btnArbol.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				//ArbolFrame panel = new ArbolFrame();
				//panel.setRaiz(raiz);
				//panel.setFunciones(funciones);
				//panel.setVisible(true);
				
				//panel.setContentPane(raiz.getdibujo());
			}
		});
		/**
		 * ----------- BOTON ASSEMBLER ---------------
		 */
		final JButton btnAssembler = new JButton("Assembler");
		btnAssembler.setEnabled(false);
		btnAssembler.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO 
				JFileChooser fc = new JFileChooser(".");		
				fc.setDialogTitle("Save File");			
				fc.setMultiSelectionEnabled(false);
				int option = fc.showSaveDialog(btnAssembler);
				if (option == JFileChooser.APPROVE_OPTION){
					try {
						String path = fc.getSelectedFile().getAbsolutePath();
						//assembler.setPath(path);
						//assembler.ejecutable();
						//console.append(assembler.getConsola());
						//File file = assembler.getArchivo();
						//System.out.println("ss- "+file.getAbsolutePath());
						
					}catch (Exception ex){ 
						ex.printStackTrace(); 
						} 	
	
					
				}
			}
		});
		/**
		 * ----------- BOTON RUN ---------------
		 */
		final JButton btnRun = new JButton("");
		btnRun.setEnabled(false);
		btnRun.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				clear();
						
				String program = textArea.getText();
				//Table table=new Table();

				//Parser parser = new Parser(program,table);
				Parser parser = new Parser();
				//parser.Parse();
	
				//-----------//
				
				//Ininicializo estructuras para mostrar las salidas.
				AnalizadorLexico analizer = parser.getLexico();
				//LexicalAnalizer analizer = new LexicalAnalizer(program, table);
				Vector<Token> tokens = analizer.getListaTokens();
				
				System.out.println("CANT TOK: "+tokens.size());
		        //ArrayList<SintacticStructure> structures = parser.getReglas();
		        //System.out.println("CANT REGLAS: "+structures.size());
				//ArrayList<Error> errors = analizer.getErrors();
				//System.out.println("CANT ERRORES: "+errors.size());
				//ArrayList<TableRecord> records = table.getElements();
				//System.out.println("CANT RECORD: "+records.size());
				//ArbolSintactico arbol = parser.getArbol();
				//arbol.imprimirArbol();
				
				//errors.addAll(parser.getErrors());
				//Collections.sort(errors);
				problems.setText(null);
				
				
				
				/*for(Error error : errors){
					drawLine(error.nroLine-1, error.severity);
					problems.setText(problems.getText() + error.description + " En linea: "+error.nroLine+"\n");
				}*/
				

				//Panel tokens agrego filas a la tabla.
				for(Token token : tokens){
					String[] data=new String[4];
					data[0]=""+token.getId();
					//data[1]=token.getType();
					data[2]=token.getLexema();
					//data[3]=""+token.getNroLine();
					dtm.addRow(data);
				}
				
				/*for (SintacticStructure struc : structures){
					String[] data=new String[4];
					data[0]=""+struc.getLine();
					data[1]=struc.getType();
					data[2]=struc.getDescription();
					structDtm.addRow(data);
				}
				
				for (TableRecord tr : records){
					String[] data=new String[5];
					data[0]=tr.getType();
					data[1]=tr.getLexema();
		            data[2]=String.valueOf((tr.getRef()));
		            data[3]=tr.getUso();
		            data[4]=tr.getAmbito();
		            tableDtm.addRow(data);
				}
			funciones = parser.getFunciones();
			if (errors.size()==0){
				if (raiz != null){
					//parser.getRaiz().imprimirNodo();
					assembler = new Assembler(parser, program);
					btnArbol.setEnabled(true);
					btnAssembler.setEnabled(true);
				}
				console.setText("El programa compilo correctamente. \n");
			}else{
				console.setText("El programa no pudo compilar ya que tuvo "+errors.size()+" errores \n");
			}*/
			/*
			for (Nodo n : parser.getFunciones()){
				System.out.println("******* funcion : "+n.getLexema()+" *******");
				n.imprimirNodo();
			}
			*/
			
			
			}	
		});
		//btnRun.setIcon(new ImageIcon(Interfaz.class.getResource("/icons/lrun_obj.gif")));
		
		/**
		 * ----------- BOTON OPEN ---------------
		 */
		final JButton btnOpen = new JButton("Open");
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setEnabled(true);
				btnRun.setEnabled(true);
				JFileChooser fc = new JFileChooser(".");
		
				fc.setDialogTitle("Open a File");
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de Texto", "txt");
				fc.setFileFilter(filter);
				fc.setMultiSelectionEnabled(false);
				int option = fc.showOpenDialog(btnOpen);
				if (option == JFileChooser.APPROVE_OPTION){
					File file =fc.getSelectedFile();
					try {
						BufferedReader br=new BufferedReader(new FileReader(file));
						String text="";
						String line="";
						while ((line=br.readLine())!=null){
							text+=line+"\n";							
						}
						textArea.setText(text);
						if (br!=null){br.close();}
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
				}
			}
		});
		//btnOpen.setIcon(new ImageIcon(Interfaz.class.getResource("/icons/file_obj.gif")));
		
		
		/**
		 * ----------- BOTON NEW ---------------
		 */
		JButton btnNewButton = new JButton("New");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textArea.setEnabled(true);
				btnRun.setEnabled(true);
			}
		});
		/**
		 * ----------- BOTON CLEAR ---------------
		 */
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textArea.setText("");
				textArea.setEnabled(false);
				console.setText("");
				btnArbol.setEnabled(false);
				btnAssembler.setEnabled(false);
				btnRun.setEnabled(false);
				btnArbol.setEnabled(false);
				clear();
				
			}
		});
		
	
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnOpen, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnRun, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnArbol)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnAssembler)
							.addPreferredGap(ComponentPlacement.RELATED, 512, Short.MAX_VALUE)
							.addComponent(btnClear))
						.addComponent(tabbedPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 954, Short.MAX_VALUE)
						.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 954, Short.MAX_VALUE))
					.addGap(8))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnNewButton)
							.addComponent(btnOpen))
						.addComponent(btnRun)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnArbol)
							.addComponent(btnClear)
							.addComponent(btnAssembler)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		//panel.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{textArea, btnNewButton, btnOpen, btnRun, tabbedPane, Problems, scrollPane_Problems, problems, Console, scrollPane_Console, console, Tokens, scrollPane_Tokens, table, scrollPane, lines}));
		
	}
	private void clear(){
		//this.textArea.setText(null);
		structDtm.setRowCount(0);
		tableDtm.setRowCount(0);
		dtm.setRowCount(0);
		problems.setText("");
	}


	private void drawLine(int numberLine, int severity){

	try {
        //PINTA LINEA EN AREA DE TEXTO
		int startLineText = textArea.getLineStartOffset(numberLine);
		int endLineText = textArea.getLineEndOffset(numberLine);
		switch(severity){
			/*case Error.error:
				textArea.getHighlighter().addHighlight(startLineText, endLineText,painterTextArea_red);
			case Error.warning:
				textArea.getHighlighter().addHighlight(startLineText, endLineText,painterTextArea_yellow);*/
		}

		// PINTA LINEA EN LA COLUMNA LINEA
		int startLineLines = lines.getLineStartOffset(numberLine);
		int endLineLines = lines.getLineEndOffset(numberLine);
		painterLines = new DefaultHighlighter.DefaultHighlightPainter(Color.RED);
		lines.getHighlighter().addHighlight(startLineLines, endLineLines, painterLines);

	} catch (BadLocationException e) {
			e.printStackTrace();
		}


	}
}
