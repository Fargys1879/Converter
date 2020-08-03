package com.example.converter.controlers;
import com.example.converter.models.User;
import com.example.converter.models.Valute;
import com.example.converter.repositories.ValuteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

@Controller
public class MainController {
    @Autowired
    private ValuteRepository valuteRepository;

    private static List<Valute> listValutes = new ArrayList<>();

    @GetMapping("/")
    public String homeGet(@AuthenticationPrincipal User user, Model model) throws ParserConfigurationException, SAXException, IOException {
        /*
        // Получение фабрики, чтобы после получить билдер документов.
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        // Получили из фабрики билдер, который парсит XML, создает структуру Document в виде иерархического дерева.
        DocumentBuilder builder = factory.newDocumentBuilder();

        // Запарсили XML, создав структуру Document. Теперь у нас есть доступ ко всем элементам, каким нам нужно.
        Document document = builder.parse(new File("F:/new.xml"));
        // Получение списка всех элементов ValuteID внутри корневого элемента (getDocumentElement возвращает ROOT элемент XML файла).
        NodeList valuteIDElements = document.getDocumentElement().getElementsByTagName("Valute");


        // Перебор всех элементов valuteIDElements
        for (int i = 0; i < valuteIDElements.getLength(); i++) {
            Node valute = valuteIDElements.item(i);


            // Получение атрибутов каждого элемента
            NamedNodeMap attributes = valute.getAttributes();

            // Атрибут - тоже Node, потому нам нужно получить значение атрибута с помощью метода getNodeValue()
            listValutes.add(new Valute(attributes.getNamedItem("ID").getNodeValue()));

        }

        // Вывод информации о каждой валюте
        Iterator<Valute> iterator = listValutes.listIterator();
        while (iterator.hasNext()) {
            Valute valute = iterator.next();
            System.out.print(valute.getIdValute() + " ");
           // System.out.print(valute.getCharCode() + " ");
           // System.out.println(valute.getNumCode() + " ");
        }
        */
        //////////////////////////////////////////////////////////
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();

        AdvancedXMLHandler handler = new AdvancedXMLHandler();
        parser.parse(new File("F:/new.xml"), handler);

        for (Valute valute : listValutes)
            System.out.println(String.format("Char Code: %s,", valute.getCharCode() ));
        ////////////////////////////////////////////////////////////



        Valute kro = new Valute("R01770",(short) 752,"SEK",10,"Шведских крон", 83.6039f);
        Valute rub = new Valute("R07777",(short) 777,"RUB",1,"Рубль", 1.00f);
        valuteRepository.save(kro);
        valuteRepository.save(rub);
        List<String> list = new ArrayList<>();
        Iterable<Valute> valutes = valuteRepository.findAll();
        Iterator<Valute> valuteIterator = valutes.iterator();
        while (valuteIterator.hasNext()) {
            Valute valute = valuteIterator.next();
            list.add(valute.getCharCode());
        }
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
        return "home";
    }

    @PostMapping("/home/convert")
    public String homePost(@Valid String valuteInput,
                           @Valid String valuteOutput,
                           @RequestParam int inputValue,
                           Model model) {

        Iterable<Valute> valutes = valuteRepository.findAll();
        Iterator<Valute> valuteIterator = valutes.iterator();
        int inputNominal = 1;
        int outputNominal = 1;
        double outputValue = 0;
        while (valuteIterator.hasNext()) {

            Valute valute = valuteIterator.next();

            if (valute.getCharCode().equals(valuteInput)) {
                inputNominal = valute.getNominal();
                outputValue = valute.getValue();
            }

            if (valute.getCharCode().equals(valuteOutput)) {
                outputNominal = valute.getNominal();


            }

        }
        //outputValue = outputValue * (inputValue * outputNominal)/inputNominal;
        model.addAttribute("outputValue",outputValue);
        System.out.println(valuteInput);
        System.out.println(valuteOutput);
        System.out.println(outputValue);
        return "redirect:/home";
    }
    private static class AdvancedXMLHandler extends DefaultHandler {
        private String numCode, charCode, nominal, name, value, lastElementName;

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
                if (lastElementName.equals("NumCode"))
                    numCode = information;
                if (lastElementName.equals("CharCode"))
                    charCode = information;
                if (lastElementName.equals("Nominal"))
                    nominal = information;
                if (lastElementName.equals("Name"))
                    name = information;
                if (lastElementName.equals("Value"))
                    value = information;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if ( (charCode != null && !charCode.isEmpty())&&
                    (numCode != null && !numCode.isEmpty())&&
                    (nominal != null && !nominal.isEmpty())&&
                    (name != null && !name.isEmpty())&&
                    (value != null && !value.isEmpty()) )
                    {
                listValutes.add(new Valute(Short.parseShort(numCode), charCode, Integer.parseInt(nominal), name, Double.parseDouble(value)));
                numCode = null;
                charCode = null;
                nominal = null;
                name = null;
                value = null;
            }
        }
    }

}
