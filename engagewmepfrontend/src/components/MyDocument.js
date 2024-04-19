import React from "react";
import { Document, Page, Text, View, StyleSheet } from "@react-pdf/renderer";

const formatColumnName = (columnName) => {
	return columnName
		.replace(/([A-Z])/g, " $1")
		.replace(/_/g, " ")
		.trim()
		.replace(/^\w/, (c) => c.toUpperCase());
};

const MyDocument = ({
	students,
	selectedEvents,
	fetchStrategy,
	selectedColumns,
}) => (
	<Document>
		<Page size="A4">
			<View style={styles.page}>
				<View style={styles.section}>
					<Text style={styles.header}>Students Details</Text>
					<Text style={styles.info}>
						Selected Events:{" "}
						{selectedEvents.map((event) => event.name).join(", ")}
					</Text>
					<Text style={styles.info}>Fetch Strategy: {fetchStrategy}</Text>
					<Text style={styles.info}>Number of students: {students.length}</Text>
					<View style={styles.table}>
						<View style={styles.tableRow}>
							{Object.keys(selectedColumns).map(
								(columnName) =>
									selectedColumns[columnName] && (
										<View style={styles.tableColHeader} key={columnName}>
											<Text style={styles.tableCell}>
												{formatColumnName(columnName)}
											</Text>
										</View>
									)
							)}
						</View>
						{students.map((student, index) => (
							<View style={styles.tableRow} key={index}>
								{Object.keys(selectedColumns).map((columnName) => {
									if (selectedColumns[columnName]) {
										return (
											<View
												style={styles.tableCol}
												key={`${columnName}-${index}`}
											>
												<Text style={styles.tableCell}>
													{student[columnName] !== null
														? student[columnName]
														: "N/A"}
												</Text>
											</View>
										);
									}
									return null;
								})}
							</View>
						))}
					</View>
				</View>
			</View>
		</Page>
	</Document>
);

const styles = StyleSheet.create({
	page: {
		flexDirection: "row",
		backgroundColor: "#E4E4E4",
	},
	section: {
		margin: 10,
		padding: 10,
		flexGrow: 1,
	},
	header: {
		fontSize: 20,
		marginBottom: 10,
		fontWeight: "bold",
		textAlign: "center",
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
	tableColHeader: {
		width: "10%",
		borderStyle: "solid",
		borderWidth: 1,
		borderLeftWidth: 0,
		borderTopWidth: 0,
	},
	tableCol: {
		width: "10%",
		borderStyle: "solid",
		borderWidth: 1,
		borderLeftWidth: 0,
		borderBottomWidth: 0,
	},
	tableCell: {
		margin: "auto",
		marginTop: 5,
		fontSize: 10,
		wordWrap: "break-word",
		maxWidth: "100%",
		minWidth: "100px",
		whiteSpace: "pre-wrap",
		padding: "5px",
	},
});

export default MyDocument;
