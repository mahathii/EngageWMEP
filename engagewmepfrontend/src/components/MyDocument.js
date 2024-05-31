import React from "react";
import { Page, Text, View, Document, StyleSheet } from "@react-pdf/renderer";

// Calculate the column width based on the selected columns
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

	// Dynamic styles based on the columnWidth
	const dynamicStyles = StyleSheet.create({
		tableColHeader: {
			width: columnWidth,
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
		// ... other dynamic styles if necessary
	});

	// Static styles
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
			fontWeight: 600,
		},
		tableCell: {
			margin: 5,
			fontSize: 10,
		},
	});

	return (
		<Document>
			<Page size="A4" style={staticStyles.page}>
				<View style={staticStyles.section}>
					<Text style={staticStyles.title}>Students Report</Text>
					<Text>Total Students: {students.length}</Text>
					<Text>
						Selected Events:{" "}
						{selectedEvents.map((event) => event.name).join(", ")}
					</Text>
					<Text>Fetch Strategy: {fetchStrategy}</Text>
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
