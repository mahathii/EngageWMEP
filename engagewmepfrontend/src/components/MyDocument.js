import React from "react";
import { Page, Text, View, Document, StyleSheet, Font } from "@react-pdf/renderer";

// Register Montserrat font from a reliable source or local path
Font.register({
    family: 'Montserrat',
    fonts: [
        { src: 'https://fonts.gstatic.com/s/montserrat/v15/JTURjIg1_i6t8kCHKm45_dJE3gnD-w.ttf' }, // Regular Montserrat
        { src: 'https://fonts.gstatic.com/s/montserrat/v15/JTURjIg1_i6t8kCHKm45_epG3gnD-w.ttf', fontWeight: 'bold' } // Bold Montserrat
    ]
});

// Calculate column width dynamically
const calculateColumnWidth = (selectedColumnCount) => {
    const tableWidth = 100; // Assuming 100% is the table width
    return `${tableWidth / selectedColumnCount}%`;
};

const formatColumnName = (columnName) => {
    return columnName
        .replace(/([A-Z])/g, " $1")
        .replace(/_/g, " ")
        .trim()
        .replace(/^\w/, (c) => c.toUpperCase());
};

// MyDocument Component
const MyDocument = ({
    students,
    selectedEvents,
    fetchStrategy,
    selectedColumns,
}) => {
    const selectedColumnNames = Object.keys(selectedColumns).filter(
        (column) => selectedColumns[column]
    );
    const columnWidth = calculateColumnWidth(selectedColumnNames.length);

    // Dynamic styles for columns
    const dynamicStyles = StyleSheet.create({
        tableColHeader: {
            width: columnWidth,
            backgroundColor: "#CC0000", // Red header color
            borderStyle: "solid",
            borderWidth: 1,
            borderLeftWidth: 0,
            borderTopWidth: 0,
        },
        tableCol: {
            width: columnWidth,
            borderStyle: "solid",
            borderWidth: 1,
            borderLeftWidth: 0,
            borderTopWidth: 0,
        },
    });

    // Static styles with Montserrat font applied
    const staticStyles = StyleSheet.create({
        page: {
            flexDirection: "column",
            backgroundColor: "#E4E4E4",
        },
        section: {
            margin: 10,
            padding: 10,
            flexGrow: 1,
        },
        title: {
            fontSize: 18,
            textAlign: "center",
            marginBottom: 20,
            fontFamily: 'Montserrat', // Apply Montserrat
        },
        heading: {
            fontSize: 14,
            fontFamily: 'Montserrat',
        },

        content: {
            fontSize: 14,
            fontFamily: 'Montserrat',
        },

        table: {
            display: "table",
            width: "auto",
            borderStyle: "solid",
            borderWidth: 1,
            borderRightWidth: 0,
            borderBottomWidth: 0,
        },
        tableRow: {
            margin: "auto",
            flexDirection: "row",
        },
        tableCellHeader: {
            margin: 5,
            fontSize: 12,
            fontWeight: "bold",
            color: "white", // White text for headers
            fontFamily: 'Montserrat', // Apply Montserrat to headers
        },
        tableCell: {
            margin: 5,
            fontSize: 10,
            fontFamily: 'Montserrat' // Apply Montserrat to regular cells
        },
    });

    return (
        <Document>
            <Page size="A4" style={staticStyles.page}>
                <View style={staticStyles.section}>
                    <Text style={staticStyles.title}>Students Report</Text>
                    <Text style={staticStyles.heading}>Total Students: {students.length}</Text>
                    <Text style={staticStyles.content}>
                        Selected Events:{" "}
                        {selectedEvents.map((event) => event.name).join(", ")}
                    </Text>
                    <View style={staticStyles.table}>
                        <View style={staticStyles.tableRow}>
                            {selectedColumnNames.map((columnName) => (
                                <View style={dynamicStyles.tableColHeader} key={columnName}>
                                    <Text style={staticStyles.tableCellHeader}>
                                        {formatColumnName(columnName)}
                                    </Text>
                                </View>
                            ))}
                        </View>
                        {students.map((student, index) => (
                            <View style={staticStyles.tableRow} key={index}>
                                {selectedColumnNames.map((columnName) => (
                                    <View
                                        style={dynamicStyles.tableCol}
                                        key={`${columnName}-${index}`}
                                    >
                                        <Text style={staticStyles.tableCell}>
                                            {student[columnName] || "N/A"}
                                        </Text>
                                    </View>
                                ))}
                            </View>
                        ))}
                    </View>
                </View>
            </Page>
        </Document>
    );
};

export default MyDocument;
