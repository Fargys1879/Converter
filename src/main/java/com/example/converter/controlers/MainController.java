package com.example.converter.controlers;
import com.example.converter.models.History;
import com.example.converter.models.User;
import com.example.converter.models.Valute;
import com.example.converter.repositories.HistoryRepository;
import com.example.converter.repositories.ValuteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.validation.Valid;
import javax.xml.parsers.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.text.*;
import java.util.regex.Pattern;


@Controller
public class MainController {
    @Autowired
    private ValuteRepository valuteRepository;
    @Autowired
    private HistoryRepository historyRepository;

    private static List<Valute> listValutes = new ArrayList<>();
    static float outputCourse = 1;

    @GetMapping("/")
    public String homeGet(Model model) throws ParserConfigurationException, SAXException, IOException {

        ///////////////////////Вызываем переопоеделенный AdvancedXMLHandler///////////////////////////////////
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();

        AdvancedXMLHandler handler = new AdvancedXMLHandler();
        parser.parse(new File("F:/new.xml"), handler);

        for (Valute valute : listValutes)
            //System.out.println(String.format("Char Code: %s,", valute.getCharCode() ));
            valuteRepository.save(valute);
        //////////////////////Сохранили в БД основные поля между тэгов из XML//////////////////////////////////////
        //////////////////////Вытаскиваем Valute ID из тэга XML через DocumentBuilderFactory////////////////////////////////////
        // Получение фабрики, чтобы после получить билдер документов.
        DocumentBuilderFactory factory1 = DocumentBuilderFactory.newInstance();
        // Получили из фабрики билдер, который парсит XML, создает структуру Document в виде иерархического дерева.
        DocumentBuilder builder = factory1.newDocumentBuilder();
        // Запарсили XML, создав структуру Document. Теперь у нас есть доступ ко всем элементам, каким нам нужно.
        Document document = builder.parse(new File("F:/new.xml"));
        // Получение списка всех элементов ValuteID внутри корневого элемента (getDocumentElement возвращает ROOT элемент XML файла).
        NodeList valuteIDElements = document.getDocumentElement().getElementsByTagName("Valute");
        // Перебор всех элементов valuteIDElements
        for (int i = 0; i < valuteIDElements.getLength(); i++) {
            Node valutenode = valuteIDElements.item(i);
            // Получение атрибутов каждого элемента
            NamedNodeMap attributes = valutenode.getAttributes();
            // Атрибут - тоже Node, потому нам нужно получить значение атрибута с помощью метода getNodeValue()
            listValutes.get(i).setIdValute(attributes.getNamedItem("ID").getNodeValue());
        }
        // Вывод информации о каждой валюте
        Iterator<Valute> iterator = listValutes.listIterator();
        while (iterator.hasNext()) {
            Valute valute = iterator.next();
            System.out.print(valute.getIdValute() + " ");
            System.out.print(valute.getCharCode() + " ");
            System.out.println(valute.getNumCode() + " ");
            valuteRepository.save(valute);
        }
        //////////Создание списка для сокращений валют для выпадающей формы//////////
        List<String> list = new ArrayList<>();
        Iterable<Valute> valutes = valuteRepository.findAll();
        Iterator<Valute> valuteIterator = valutes.iterator();
        while (valuteIterator.hasNext()) {
            Valute valute = valuteIterator.next();
            list.add(valute.getCharCode());
        }
        model.addAttribute("outputCourse", outputCourse);
        model.addAttribute("list", list);
        return "home";
    }
    @GetMapping("/home")
    public String homePost(Model model) {
        List<String> list = new ArrayList<>();
        Iterable<Valute> valutes = valuteRepository.findAll();
        Iterator<Valute> valuteIterator = valutes.iterator();

        while (valuteIterator.hasNext()) {
            Valute valute = valuteIterator.next();
            list.add(valute.getCharCode());
        }
        model.addAttribute("list", list);
        model.addAttribute("outputCourse", outputCourse);
        return "home";
    }

    @PostMapping("/home/convert")
    public String homePost(@Valid String valuteInput,
                           @Valid String valuteOutput,
                           @RequestParam int inputCount,
                           Model model) {

        Iterable<Valute> valutes = valuteRepository.findAll();
        Iterator<Valute> valuteIterator = valutes.iterator();
        int inputNominal = 1;
        int outputNominal = 1;
        float inputValue = 0;
        float outputValue = 0;
        while (valuteIterator.hasNext()) {

            Valute valute = valuteIterator.next();

            if (valute.getCharCode().equals(valuteInput)) {
                inputNominal = valute.getNominal();
                inputValue = valute.getValue();
            }

            if (valute.getCharCode().equals(valuteOutput)) {
                outputNominal = valute.getNominal();
                outputValue = valute.getValue();
            }

        }
        outputCourse = (inputValue * inputNominal)/(outputValue * outputNominal);
        model.addAttribute("outputCourse",outputCourse);
        System.out.println(inputValue);
        System.out.println(outputValue);
        System.out.println(outputCourse);
        Date date = new Date();
        System.out.println(date.getDate());
        History history = new History(valuteInput,valuteOutput,outputCourse,inputCount,date);
        historyRepository.save(history);
        return "redirect:/home";
    }
    /*
    @PostMapping("/saveBook")
    public ResponseEntity<Object> addBook(@RequestBody Book book) {
        bookStore.add(book);
        ServiceResponse<Book> response = new ServiceResponse<Book>("success", book);
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }*/

    //////////////////////////////////////////////////////////////////////////////////
    private static class AdvancedXMLHandler extends DefaultHandler {
        private String id, numCode, charCode, nominal, name, valuestr, lastElementName;

        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) throws SAXException {
            lastElementName = qName;
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            String information = new String(ch, start, length);

            information = information.replace("\n", "").trim();

            if (!information.isEmpty()) {
                if (lastElementName.equals("Valute ID"))
                    id = information;
                if (lastElementName.equals("NumCode"))
                    numCode = information;
                if (lastElementName.equals("CharCode"))
                    charCode = information;
                if (lastElementName.equals("Nominal"))
                    nominal = information;
                if (lastElementName.equals("Name"))
                    name = information;
                if (lastElementName.equals("Value"))
                    valuestr = information;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {

            if ( (charCode != null && !charCode.isEmpty())&&
                    (numCode != null && !numCode.isEmpty())&&
                    (nominal != null && !nominal.isEmpty())&&
                    (valuestr != null && !valuestr.isEmpty())&&
                    (name != null && !name.isEmpty())) {
                String[] values = customFormat(valuestr);
                //Перевод из строковых частей в float число
                float value = 0;
                for (int i = 0; i < values.length; i++) {
                    if (i == 0) {
                        value += Float.parseFloat(values[i]);
                    } else {
                        float tmp = (  Integer.parseInt(values[i]) * (float) 0.0001);
                        value += tmp ; }
                }
                //System.out.println(value);
                listValutes.add(new Valute(id, numCode, charCode, Integer.parseInt(nominal), name, value ));
                id = null;
                numCode = null;
                charCode = null;
                nominal = null;
                name = null;
                valuestr = null;
            }
        }
        //Разбивка значение Value из XML на целые числа и числа после запятой
        static public String[] customFormat(String value) {
            Pattern pattern = Pattern.compile(",");
            String[] result = pattern.split(value);
            return result;
        }
    }

}
