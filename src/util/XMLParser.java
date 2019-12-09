package util;

import javax.xml.stream.*;
import javax.xml.stream.events.*;
import java.io.*;
import java.util.Iterator;


class XMLParser {
    private XMLEventReader eventReader;
    private GameStatus gameStatus = new GameStatus();

    GameStatus readFile(File file) {
        try {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = new FileInputStream(file);
            eventReader = inputFactory.createXMLEventReader(in);

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                if (!event.isStartElement()) {
                    continue;
                }

                StartElement startElement = event.asStartElement();
                String tag = startElement.getName().getLocalPart();
                event = eventReader.nextEvent();
                String value = event.asCharacters().getData();

                switch (tag) {
                    case "timer":
                        gameStatus.setTimerMode(value);
                        break;

                    case "turn":
                        gameStatus.setTurn(value);
                        break;

                    case "time": {
                        Iterator<Attribute> attributes = startElement.getAttributes();

                        while (attributes.hasNext()) {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals("color")) {
                                String color = attribute.getValue();

                                gameStatus.addTime(color, value);
                                break;
                            }
                        }
                        break;
                    }

                    case "piece": {
                        setPieces(startElement);
                        break;
                    }
                    default:
                        gameStatus.setValue(tag, value);
                }
            }
        } catch (FileNotFoundException | XMLStreamException e) {
            e.printStackTrace();
        }
        return gameStatus;
    }

    private void setPieces(StartElement startElement) throws XMLStreamException {
        String color = null;
        String type = null;
        int pieceRank = 0;
        String pieceFile = null;
        int doubleMoveRank = 0;
        int rankDifferenceForPawn = 0;
        int enPassantRank = 0;
        int enPassantedRank = 0;
        int movingTimes = 0;

        XMLEvent event;

        Iterator<Attribute> attributes = startElement.getAttributes();
        while (attributes.hasNext()) {
            Attribute attribute = attributes.next();
            String attrName = attribute.getName().toString();
            if (attrName.equals("color")) {
                color = attribute.getValue();
            } else if (attrName.equals("type")) {
                type = attribute.getValue();
            }
        }

        while (eventReader.hasNext()) {
            event = eventReader.nextEvent();
            if (event.isEndElement() && event.asEndElement().getName().getLocalPart().equals("piece")) {
                break;
            }

            if (!event.isStartElement()) {
                continue;
            }

            String innerTag = event.asStartElement().getName().getLocalPart();
            event = eventReader.nextEvent();
            String innerValue = event.asCharacters().getData();

            switch (innerTag) {
                case "rank":
                    pieceRank = Integer.parseInt(innerValue);
                    break;
                case "file":
                    pieceFile = innerValue;
                    break;
                case "doubleMoveRank":
                    doubleMoveRank = Integer.parseInt(innerValue);
                    break;
                case "rankDifferenceForPawn":
                    rankDifferenceForPawn = Integer.parseInt(innerValue);
                    break;
                case "enPassantRank":
                    enPassantRank = Integer.parseInt(innerValue);
                    break;
                case "enPassantedRank":
                    enPassantedRank = Integer.parseInt(innerValue);
                    break;
                case "movingTimes":
                    movingTimes = Integer.parseInt(innerValue);
                    break;
            }
        }

        gameStatus.addPiece(color, type, pieceRank, pieceFile, doubleMoveRank, rankDifferenceForPawn, enPassantRank, enPassantedRank, movingTimes);
    }

    void writeFile(GameStatus gameStatus, String fileName) throws FileNotFoundException, XMLStreamException {
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(new FileOutputStream(fileName));

        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");
        XMLEvent tab = eventFactory.createDTD("\t");

        StartDocument startDocument = eventFactory.createStartDocument();
        eventWriter.add(startDocument);

        eventWriter.add(end);
        StartElement statusStartElem = eventFactory.createStartElement("", "", "status");
        eventWriter.add(statusStartElem);
        eventWriter.add(end);

        createNode(eventWriter, "turn", gameStatus.getTurn().toString());

        // add general preferences
        for (String preferenceName : GameStatus.preferenceNames) {
            createNode(eventWriter, preferenceName, gameStatus.getValue(preferenceName));
        }

        // add times
        // - EXAMPLE
        // <time color="BLACK">00:01:10</time>
        for (GameStatus.Time time : gameStatus.getTimes()) {
            eventWriter.add(tab);
            eventWriter.add(eventFactory.createStartElement("", "", "time"));

            eventWriter.add(eventFactory.createAttribute("color", time.getColor().toString()));
            eventWriter.add(eventFactory.createCharacters(time.getTime()));

            eventWriter.add(eventFactory.createEndElement("", "", "time"));
            eventWriter.add(end);
        }

        // add pieces

        /* - EXAMPLE
        <piece color="WHITE" type="QUEEN">
            <rank>8</rank>
            <file>h</file>
        </piece> */
        for (GameStatus.Piece piece : gameStatus.getPieceObjs()) {
            eventWriter.add(tab);
            eventWriter.add(eventFactory.createStartElement("", "", "piece"));

            eventWriter.add(eventFactory.createAttribute("color", piece.getColor().toString()));
            eventWriter.add(eventFactory.createAttribute("type", piece.getType().toString()));
            eventWriter.add(end);

            eventWriter.add(tab);
            createNode(eventWriter, "rank", String.valueOf(piece.getRank()));
            eventWriter.add(tab);
            createNode(eventWriter, "file", piece.getFile());

            eventWriter.add(tab);
            createNode(eventWriter, "doubleMoveRank", String.valueOf(piece.getDoubleMoveRank()));
            eventWriter.add(tab);
            createNode(eventWriter, "rankDifferenceForPawn", String.valueOf(piece.getRankDifferenceForPawn()));
            eventWriter.add(tab);
            createNode(eventWriter, "enPassantRank", String.valueOf(piece.getEnPassantRank()));
            eventWriter.add(tab);
            createNode(eventWriter, "enPassantedRank", String.valueOf(piece.getEnPassantedRank()));
            eventWriter.add(tab);
            createNode(eventWriter, "movingTimes", String.valueOf(piece.getMovingTimes()));

            eventWriter.add(tab);
            eventWriter.add(eventFactory.createEndElement("", "", "piece"));
            eventWriter.add(end);
        }

        eventWriter.add(eventFactory.createEndElement("", "", "status"));
        eventWriter.add(end);
        eventWriter.add(eventFactory.createEndDocument());
        eventWriter.close();
    }

    private void createNode(XMLEventWriter eventWriter, String name, String value) throws XMLStreamException {
        startNode(eventWriter, name, false);

        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        Characters characters = eventFactory.createCharacters(value);
        eventWriter.add(characters);

        EndElement eElement = eventFactory.createEndElement("", "", name);
        eventWriter.add(eElement);

        XMLEvent end = eventFactory.createDTD("\n");
        eventWriter.add(end);
    }

    private void startNode(XMLEventWriter eventWriter, String name, boolean addEnd) throws XMLStreamException {
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent tab = eventFactory.createDTD("\t");

        StartElement sElement = eventFactory.createStartElement("", "", name);
        eventWriter.add(tab);
        eventWriter.add(sElement);

        if (addEnd) {
            XMLEvent end = eventFactory.createDTD("\n");
            eventWriter.add(end);
        }
    }
}